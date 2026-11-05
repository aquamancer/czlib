package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.internal.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onOpenScreen(Lnet/minecraft/network/packet/s2c/play/OpenScreenS2CPacket;)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V"))
    private void onOpenScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
        UpdateManager.getInstance().onOpenScreenPacket(packet);
        SelfIdentifier.onOpenScreenPacket(packet);
        VzcParser.onOpenScreenPacket(packet);
    }

    @Inject(method = "onCloseScreen(Lnet/minecraft/network/packet/s2c/play/CloseScreenS2CPacket;)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V"))
    private void onCloseScreenPacket(CloseScreenS2CPacket packet, CallbackInfo ci) {
        UpdateManager.getInstance().onCloseScreenPacket(packet);
    }

    @Inject(method = "onInventory(Lnet/minecraft/network/packet/s2c/play/InventoryS2CPacket;)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V"))
    private void onInventory(InventoryS2CPacket packet, CallbackInfo ci) {
        // for some reason the server sends 2 of the same inventory packets on a non-command opened gui
        TrinketParser.onInventoryS2CPacket(packet);
        TrinketLocator.onInventoryS2CPacket(packet);
        VzcParser.onInventoryPacket(packet);
    }

    @Inject(method = "onOverlayMessage(Lnet/minecraft/network/packet/s2c/play/OverlayMessageS2CPacket;)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V"))
    private void onActionBarMessage(OverlayMessageS2CPacket packet, CallbackInfo ci) {
        UpdateManager.getInstance().onActionBarMessage(packet.getMessage());
    }

    // called on armor/mainhand swaps for other players and entering render distance
    @Inject(at = @At(value="INVOKE", target="Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;getEquipmentList()Ljava/util/List;"),
            method = "onEntityEquipmentUpdate(Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;)V"
    )
    private void onArmorChange(EntityEquipmentUpdateS2CPacket packet, CallbackInfo ci) {
        UpdateManager.getInstance().onArmorChange(packet);
    }

    @Inject(method = "onGameMessage(Lnet/minecraft/network/packet/s2c/play/GameMessageS2CPacket;)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (packet.overlay()) return;
        // after selecting a tree in room 0, the server sends the chat and close screen packet at the same time
        // but the chat packet is always first, causing TREE_SELECT event to be fired while the screen is still opened
        // client.send() is used here as a hack to enqueue the TREE_SELECT invocation to the main thread executor queue
        // which will cause it to run after the close screen handling
        MinecraftClient.getInstance().send(() -> {
            ChatParser.onChatMessage(packet.content());
        });
    }

    @Inject(method = "onPlayerListHeader(Lnet/minecraft/network/packet/s2c/play/PlayerListHeaderS2CPacket;)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V"))
    private void onTabListHeader(PlayerListHeaderS2CPacket packet, CallbackInfo ci) {

    }
}
