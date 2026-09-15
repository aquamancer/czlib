package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.api.bosses.Boss;
import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(targets = "net.minecraft.network.packet.s2c.play.BossBarS2CPacket$AddAction")
public class BossBarMixin {
    @Unique
    private static final Pattern GRAVE = Pattern.compile("(\\w+)'s Grave*");

    // boss strings have §formatting chars before the name so a leading .* must be used
    @Unique
    private static final Pattern CALLI = Pattern.compile(".*Callicarpa,.*");

    @Unique
    private static final Pattern BROOD = Pattern.compile(".*The Broodmother.*");

    @Unique
    private static final Pattern VESP = Pattern.compile(".*The Vesperidys.*");

    @Unique
    private static final Map<Pattern, Consumer<Matcher>> operations = Map.of(
            GRAVE, (matcher) -> ZenithApiStateEvents.GRAVE_SPAWNED.invoker().onGraveSpawn(matcher.group(1)),
            CALLI, (matcher) -> ZenithApiStateEvents.BOSS_SPAWNED.invoker().onBossEvent(Boss.CALLICARPA),
            BROOD, (matcher) -> ZenithApiStateEvents.BOSS_SPAWNED.invoker().onBossEvent(Boss.BROODMOTHER),
            VESP, (matcher) -> ZenithApiStateEvents.BOSS_SPAWNED.invoker().onBossEvent(Boss.VESPERIDYS)
    );

    @Shadow
    private Text name;
    @Inject(at=@At("HEAD"), method="accept")
    private void onBossBarAdd(UUID uuid, BossBarS2CPacket.Consumer consumer, CallbackInfo ci) {
        String title = name.getString();
        for (Map.Entry<Pattern, Consumer<Matcher>> op : operations.entrySet()) {
            Matcher matcher = op.getKey().matcher(title);
            if (matcher.find()) {
                op.getValue().accept(matcher);
                break;
            }
        }
    }
}
