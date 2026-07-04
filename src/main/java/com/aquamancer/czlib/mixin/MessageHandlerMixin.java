package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.trinket.ChatParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {
    @Inject(at = @At("HEAD"), method = "onGameMessage(Lnet/minecraft/text/Text;Z)V")
    private void onGameMessage(Text message, boolean overlay, CallbackInfo ci) {
        MinecraftClient.getInstance().execute(() -> {
            ChatParser.onChatMessage(message);
        });
    }
}
