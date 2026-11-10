package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApiStatus.Internal
public class ShardTracker {
//    private static final Pattern SHARD_REGEX = Pattern.compile("shard:\\s*<([-\\w])>.*");
    private static final Pattern SHARD_REGEX = Pattern.compile(".*<(?<shard>[-\\w]*)>.*");
    private static final int TICKS_UNTIL_TIMEOUT = 200;

    private static boolean listening = false;
    private static int ticksUntilTimeout = TICKS_UNTIL_TIMEOUT;
    private static @Nullable String currentShard;
    private static @Nullable String lastValidShard;

    // the server resends all recent chat messages on each shard change
    // add a delay after world change before notifying listeners so they do not see these messages
    private static final int WORLD_CHANGE_DELAY_TICKS = 2;
    private static int worldChangeDelayTicks = 0;
    private static boolean shardChangeQueued = false;

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> onTick());
        ZenithApiStateEvents.WORLD_CHANGED.register((w) -> {
            if (currentShard != null) {
                lastValidShard = currentShard;
                currentShard = null;
            }
            listening = true;
            ticksUntilTimeout = TICKS_UNTIL_TIMEOUT;
            worldChangeDelayTicks = WORLD_CHANGE_DELAY_TICKS;
        });
    }

    public static void onTick() {
        if (worldChangeDelayTicks > 0) {
            worldChangeDelayTicks--;
        }
        if (worldChangeDelayTicks <= 0 && shardChangeQueued) {
            shardChangeQueued = false;
            invokeEvents();
        }

        // timeout protocol
        if (listening) {
            ticksUntilTimeout--;
            if (ticksUntilTimeout <= 0) {
                listening = false;
                ZenithApiStateEvents.ENTER_NON_ZENITH_SHARD.invoker().onShardChange(lastValidShard, null);
            }
        }
    }

    public static void onTabListHeader(String header) {
        if (!listening) return;
        String shard = parseShard(header);
        if (shard == null) return;
        listening = false;
        currentShard = shard;
        shardChanged();
    }

    private static void shardChanged() {
        if (worldChangeDelayTicks > 0) {
            shardChangeQueued = true;
        } else {
            shardChangeQueued = false;
            invokeEvents();
        }
    }

    private static void invokeEvents() {
        if (isZenithShard(currentShard)) {
            if (currentShard.equals(lastValidShard)) {
                ZenithApiStateEvents.REJOIN_ZENITH_SHARD.invoker().onRejoinZenithShard(currentShard);
            } else {
                ZenithApiStateEvents.ENTER_ZENITH_SHARD.invoker().onShardChange(lastValidShard, currentShard);
            }
        } else {
            ZenithApiStateEvents.ENTER_NON_ZENITH_SHARD.invoker().onShardChange(lastValidShard, currentShard);
        }
    }

    private static @Nullable String parseShard(String header) {
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
