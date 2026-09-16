package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import com.aquamancer.czlib.internal.event.ZenithApiInternalEvents;
import com.aquamancer.czlib.mixin.PlayerListHudAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShardTracker {
//    private static final Pattern SHARD_REGEX = Pattern.compile("shard:\\s*<([-\\w])>.*");
    private static final Pattern SHARD_REGEX = Pattern.compile(".*<(?<shard>[-\\w]*)>.*");
    private static final int ATTEMPTS_UNTIL_TIMEOUT = 5;
    private static final int ATTEMPT_INTERVAL_TICKS = 20;
    private static final int FIRST_ATTEMPT_DELAY_TICKS = 10;

    private static int remainingAttempts = 0;
    private static int ticksUntilAttempt = ATTEMPT_INTERVAL_TICKS;

    private static @Nullable String currentShard;
    private static @Nullable String previousValidShard;

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> onTick());
        ZenithApiInternalEvents.WORLD_CHANGED.register(ShardTracker::updateCurrentShard);
    }

    public static void updateCurrentShard() {
        if (currentShard != null) {
            previousValidShard = currentShard;
            currentShard = null;
        }
        remainingAttempts = ATTEMPTS_UNTIL_TIMEOUT;
        ticksUntilAttempt = FIRST_ATTEMPT_DELAY_TICKS;
	}

    public static void onTick() {
        if (remainingAttempts <= 0) return;
        if (ticksUntilAttempt > 0) {
            ticksUntilAttempt--;
            return;
        }
        remainingAttempts--;
        ticksUntilAttempt = ATTEMPT_INTERVAL_TICKS;

        currentShard = parseShard();
        if (currentShard == null) {
            if (remainingAttempts > 0) return;
            // exhausted all attempts
            if (isZenithShard(previousValidShard)) {
                ZenithApiStateEvents.EXIT_ZENITH_SHARD.invoker().onShardChange(previousValidShard, null);
            }
            ZenithApiStateEvents.ENTER_NON_ZENITH_SHARD.invoker().onShardChange(previousValidShard, null);
            return;
        }

        remainingAttempts = 0;
        if (isZenithShard(currentShard)) {
            if (currentShard.equals(previousValidShard)) {
                ZenithApiStateEvents.REJOIN_ZENITH_SHARD.invoker().onRejoinZenithShard(currentShard);
            } else {
                ZenithApiStateEvents.ENTER_ZENITH_SHARD.invoker().onShardChange(previousValidShard, currentShard);
            }
        } else {
            if (isZenithShard(previousValidShard)) {
                ZenithApiStateEvents.EXIT_ZENITH_SHARD.invoker().onShardChange(previousValidShard, currentShard);
            }
            ZenithApiStateEvents.ENTER_NON_ZENITH_SHARD.invoker().onShardChange(previousValidShard, currentShard);
        }
    }

    private static @Nullable String parseShard() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.inGameHud == null || client.inGameHud.getPlayerListHud() == null) {
            return null;
        }

        Text headerText = ((PlayerListHudAccessor) client.inGameHud.getPlayerListHud()).getHeader();
        if (headerText == null) {
            return null;
        }

        String header = headerText.getString();
        Matcher matcher = SHARD_REGEX.matcher(header);
        if (matcher.matches()) {
            String newShard = matcher.group("shard");
            if (newShard != null && !newShard.isBlank()) {
                return newShard;
            }
        }
        return null;
    }

    public static @Nullable String getCurrentShard() {
        return currentShard;
    }

    public static boolean inZenithShard() {
        if (currentShard == null) return false;
        return currentShard.startsWith("zenith");
    }

    public static boolean isZenithShard(String shard) {
        return shard != null && shard.startsWith("zenith");
    }

    private ShardTracker() {}
}
