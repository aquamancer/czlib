package com.aquamancer.czlib.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;)V")
    private void onSend(Packet<?> packet, CallbackInfo ci) {
        MinecraftClient.getInstance().execute(() -> {
//            if (packet instanceof CloseHandledScreenC2SPacket c) {
//                MinecraftClient.getInstance().player.sendMessage(Text.literal("close screen c2s: " + c.getSyncId()));
//            }
        });
    }
}
