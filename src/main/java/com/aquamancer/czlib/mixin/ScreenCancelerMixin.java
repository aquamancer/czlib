package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.internal.ScreenCanceler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
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
            if (client.currentScreen instanceof InventoryScreen) {
                // un-desync the player's currentScreenHandler but don't close the inventory
                client.player.currentScreenHandler = client.player.playerScreenHandler;
            } else if (client.currentScreen instanceof HandledScreen<?>) {
                client.currentScreen.close();
            }
//            client.player.sendMessage(Text.literal("canceled screen syncid="+client.player.currentScreenHandler.syncId));
            ci.cancel();
        }
    }
}
