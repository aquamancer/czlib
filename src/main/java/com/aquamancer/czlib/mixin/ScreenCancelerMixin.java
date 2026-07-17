package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.internal.ScreenCanceler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
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
        client.player.sendMessage(Text.literal(screen.getClass().getCanonicalName()).append(Text.literal(": ")).append(screen.getTitle()));
        if (ScreenCanceler.shouldCancelScreen(screen)) {
            ci.cancel();
        }
    }
}
