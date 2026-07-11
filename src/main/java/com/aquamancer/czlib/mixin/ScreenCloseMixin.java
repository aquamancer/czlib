package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.internal.UpdateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
    @Inject(at = @At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;closeScreen()V"), method = "closeHandledScreen()V")
    private void onManualCloseScreen(CallbackInfo ci) {
        Screen closedScreen = MinecraftClient.getInstance().currentScreen;
        UpdateManager.getInstance().onManualScreenClose(closedScreen);
    }
}
