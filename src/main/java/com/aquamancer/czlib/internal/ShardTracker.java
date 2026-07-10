package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.internal.event.ZenithApiInternalEvents;
import com.aquamancer.czlib.mixin.PlayerListHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShardTracker {
    private static final Pattern SHARD_REGEX = Pattern.compile("shard:\\s*<([-\\w])>.*");
    private static final int ATTEMPTS_UNTIL_TIMEOUT = 5;
    private static final int ATTEMPT_INTERVAL_TICKS = 500 / 20;
    private static final int FIRST_ATTEMPT_DELAY_TICKS = 250 / 20;

    private static int remainingAttempts = 0;
    private static int ticksUntilAttempt = ATTEMPT_INTERVAL_TICKS;

    private static @Nullable String currentShard;
    private static @Nullable String previousValidShard;

    static {
        ZenithApiInternalEvents.WORLD_CHANGED.register(ShardTracker::updateCurrentShard);
    }

    public static void updateCurrentShard() {
        remainingAttempts = ATTEMPTS_UNTIL_TIMEOUT;
        ticksUntilAttempt = FIRST_ATTEMPT_DELAY_TICKS;
	}

    public static void onTick() {
        if (remainingAttempts <= 0) return;
        if (ticksUntilAttempt > 0) {
            ticksUntilAttempt--;
        } else {
            remainingAttempts--;
            String newShard = parseShard();
            if (currentShard != null) {
                previousValidShard = currentShard;
            }
            currentShard = newShard;
            if (currentShard != null) {
                remainingAttempts = 0;
                if (!currentShard.equals(previousValidShard)) {  // does not call events after DC'ing+reconnecting mid-run
                    if (isZenithShard(previousValidShard)) {
                        ZenithApiInternalEvents.EXIT_ZENITH_SHARD.invoker().onExitZenithShard(previousValidShard, currentShard);
                    }
                    if (isZenithShard(currentShard)) {
                        ZenithApiInternalEvents.ENTER_ZENITH_SHARD.invoker().onEnteredZenithShard(previousValidShard, currentShard);
                    }
                }
            }
            ticksUntilAttempt = ATTEMPT_INTERVAL_TICKS;
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
            String newShard = matcher.group(1);
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
