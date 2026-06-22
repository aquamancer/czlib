package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.trinket.TrinketOpener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClickSlotMixin {
    @Inject(at = @At("TAIL"), cancellable = true, method = "send(Lnet/minecraft/network/packet/Packet;)V")
    private static void send(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ClickSlotC2SPacket clickPacket) {
            TrinketOpener.onRMB(clickPacket);
        }
    }
}
