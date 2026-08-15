package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.internal.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("HEAD"), method = "onOpenScreen(Lnet/minecraft/network/packet/s2c/play/OpenScreenS2CPacket;)V")
    private void onOpenScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(() -> {
            if (packet != null && client.player != null) {
//                client.execute(() -> client.player.sendMessage(Text.literal("Open screen packet: " + packet.getName() + ", syncId: " + packet.getSyncId()+"type="+packet.getScreenHandlerType())));
//                long now = System.nanoTime();
//                client.execute(() -> client.player.sendMessage(Text.literal("open screen "+packet.getName()+": "+now+",syncid="+packet.getSyncId())));
                UpdateManager.getInstance().onOpenScreenPacket(packet);
                SelfIdentifier.onOpenScreenPacket(packet);
                VzcParser.onOpenScreenPacket(packet);
            }
        });
    }

    @Inject(at = @At("HEAD"), method = "onInventory(Lnet/minecraft/network/packet/s2c/play/InventoryS2CPacket;)V")
    private void onInventory(InventoryS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.execute(() -> {
//                client.player.sendMessage(Text.literal("inventory packet received syncId="+packet.getSyncId()+", size="+packet.getContents().size()));
                TrinketParser.onInventoryS2CPacket(packet, client);
                TrinketLocator.onInventoryS2CPacket(packet);
                VzcParser.onInventoryPacket(packet);
            });
        }
    }

    @Inject(at = @At("HEAD"), method = "onOverlayMessage(Lnet/minecraft/network/packet/s2c/play/OverlayMessageS2CPacket;)V")
    private void onActionBarMessage(OverlayMessageS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.execute(() -> {
                UpdateManager.getInstance().onActionBarMessage(packet.getMessage());
            });
        }
    }

    // called on armor/mainhand swaps for other players and entering render distance
    @Inject(at = @At(value="INVOKE", target="Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;getEquipmentList()Ljava/util/List;"),
            method = "onEntityEquipmentUpdate(Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;)V"
    )
    private void onArmorChange(EntityEquipmentUpdateS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.execute(() -> {
                UpdateManager.getInstance().onArmorChange(packet, client);
            });
        }
    }
}
