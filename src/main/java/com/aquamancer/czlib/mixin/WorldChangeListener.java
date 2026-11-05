package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
class WorldChangeListener {
    @Inject(method = "setWorld(Lnet/minecraft/client/world/ClientWorld;)V", at = @At("HEAD"))
    private void onSetWorld(@Nullable ClientWorld world, CallbackInfo ci) {
        ZenithApiStateEvents.WORLD_CHANGED.invoker().onWorldChange(world);
    }
}
