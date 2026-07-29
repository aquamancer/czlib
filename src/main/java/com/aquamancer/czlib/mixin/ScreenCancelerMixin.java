package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.internal.ScreenCanceler;
import com.aquamancer.czlib.internal.UpdateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ScreenCancelerMixin {
    @Inject(at = @At("HEAD"), cancellable=true, method = "setScreen(Lnet/minecraft/client/gui/screen/Screen;)V")
    private void setScreen(@Nullable Screen screen, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (screen == null || client == null || client.player == null) return;
        if (ScreenCanceler.shouldCancelScreen(screen)) {
            UpdateManager.sendPacket(new CloseHandledScreenC2SPacket(client.player.currentScreenHandler.syncId));
            client.player.sendMessage(Text.literal("canceled screen syncid="+client.player.currentScreenHandler.syncId));
            if (client.currentScreen instanceof HandledScreen<?>) {
                // un-desync the player's currentScreenHandler
                client.player.currentScreenHandler = client.player.playerScreenHandler;
            }
            ci.cancel();
        }
    }
}
