package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(targets = "net.minecraft.network.packet.s2c.play.BossBarS2CPacket$AddAction")
public class BossBarMixin {
    @Unique
    private static final Pattern GRAVE = Pattern.compile("^(\\w+)'s Grave.*");

    @Shadow
    private Text name;
    @Inject(at=@At("HEAD"), method="accept")
    private void onBossBarAdd(UUID uuid, BossBarS2CPacket.Consumer consumer, CallbackInfo ci) {
        Matcher matcher = GRAVE.matcher(name.getString());
        if (!matcher.matches()) return;
        ZenithApiStateEvents.GRAVE_SPAWNED.invoker().onGraveSpawn(matcher.group(1));
    }
}
