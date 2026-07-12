package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.internal.SelfIdentifier;
import com.aquamancer.czlib.internal.UpdateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ScreenCloseMixin {
    /* ClientPlayerEntity.class:
            public void closeHandledScreen() {
                this.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(this.currentScreenHandler.syncId));
                this.closeScreen();
            }

            public void closeScreen() {
                super.closeHandledScreen();
                this.client.setScreen((Screen)null);
            }
     */
    @Unique
    private static Screen closedScreen;

    @Inject(at = @At("HEAD"), method = "closeHandledScreen()V")
    private void captureClosedScreen(CallbackInfo ci) {
        closedScreen = MinecraftClient.getInstance().currentScreen;
    }

    @Inject(at = @At("TAIL"), method = "closeHandledScreen()V")
    private void onManualCloseScreen(CallbackInfo ci) {
        UpdateManager.getInstance().onManualScreenClose(closedScreen);
    }
}
