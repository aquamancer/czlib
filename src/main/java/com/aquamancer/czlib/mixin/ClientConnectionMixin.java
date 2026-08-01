package com.aquamancer.czlib.mixin;

import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
//    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;)V")
//    private void onSend(Packet<?> packet, CallbackInfo ci) {
//        MinecraftClient.getInstance().execute(() -> {
//            if (packet instanceof ClickSlotC2SPacket p) {
//                MinecraftClient.getInstance().player.sendMessage(Text.literal("click slot syncid: " + p.getSyncId()));
//            }
//            if (packet instanceof CloseHandledScreenC2SPacket c) {
//                MinecraftClient.getInstance().player.sendMessage(Text.literal("close screen c2s: " + c.getSyncId()));
//            }
//        });
//    }
}
