package com.aquamancer.czlib.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClickSlotMixin {
    @Inject(at = @At("TAIL"), cancellable = true, method = "send(Lnet/minecraft/network/packet/Packet;)V")
    private static void send(Packet<?> p, CallbackInfo ci) {
        MinecraftClient.getInstance().execute(() -> {
//            if (p instanceof ClickSlotC2SPacket packet) {
//                MinecraftClient client = MinecraftClient.getInstance();
//                client.player.sendMessage(Text.literal("ActionType: " + packet.getActionType().name()));
//                client.player.sendMessage(Text.literal("Button: " + packet.getButton()));
//                client.player.sendMessage(Text.literal("ModifiedStacks: " + packet.getModifiedStacks().toString()));
//                client.player.sendMessage(Text.literal("Revision: " + packet.getRevision() + "\n" + "ScreenHandlerRevision: " + client.player.currentScreenHandler.getRevision()));
//                client.player.sendMessage(Text.literal("Slot: " + packet.getSlot()));
//                client.player.sendMessage(Text.literal("Stack: " + packet.getStack()));
//                client.player.sendMessage(Text.literal("SyncId: " + packet.getSyncId()));
//            }
        });
    }
}
