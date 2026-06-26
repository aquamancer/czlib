package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.trinket.TrinketParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    // packet sent on chest open, before inventory packet and chest open animation packet
    @Inject(at = @At("HEAD"), method = "onOpenScreen(Lnet/minecraft/network/packet/s2c/play/OpenScreenS2CPacket;)V")
    private void onOpenScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (packet != null && client.player != null) {
            client.execute(() -> client.player.sendMessage(Text.literal("Open screen packet: " + packet.getName() + ", syncId: " + packet.getSyncId())));
//            client.execute(() -> client.player.sendMessage(Text.literal(String.valueOf(System.currentTimeMillis()))));
        }
    }

    @Inject(at = @At("HEAD"), method = "onInventory(Lnet/minecraft/network/packet/s2c/play/InventoryS2CPacket;)V")
    private void onInventory(InventoryS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.execute(() -> {
                TrinketParser.onInventoryS2CPacket(packet, client);
            });
        }
    }
}
