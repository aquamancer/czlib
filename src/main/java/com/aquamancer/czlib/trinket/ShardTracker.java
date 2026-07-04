package com.aquamancer.czlib.trinket;

import com.aquamancer.czlib.mixin.PlayerListHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShardTracker {
    private static final Pattern SHARD_REGEX = Pattern.compile(".*<(?<shard>[-\\w]*)>.*");
    private static final int ATTEMPTS_UNTIL_TIMEOUT = 5;
    private static final int ATTEMPT_INTERVAL_TICKS = 1000 / 20;
    private static final int FIRST_ATTEMPT_DELAY_TICKS = 200 / 20;

    private static int remainingAttempts = 0;
    private static int ticksUntilAttempt = ATTEMPT_INTERVAL_TICKS;

    private static String currentShard;

    static {
        WorldChangeTracker.register((world) -> updateCurrentShard());
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
            attemptUpdate();
            ticksUntilAttempt = ATTEMPT_INTERVAL_TICKS;
        }
    }

    private static void attemptUpdate() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.inGameHud == null || client.inGameHud.getPlayerListHud() == null) {
            return;
        }

        Text headerText = ((PlayerListHudAccessor) client.inGameHud.getPlayerListHud()).getHeader();
        if (headerText == null) {
            currentShard = "";
            return;
        }

        String header = headerText.getString();
        Matcher matcher = SHARD_REGEX.matcher(header);
        if (matcher.matches()) {
            currentShard = matcher.group("shard");
            remainingAttempts = 0;
        } else {
            currentShard = "";
        }
    }

    public static String getCurrentShard() {
        return currentShard;
    }

    public static boolean notInZenith() {
        return !currentShard.startsWith("zenith");
    }

//	public static String getShortShard() {
//		return getCurrentShard().replaceFirst("-\\d+$", "");
//	}

    private ShardTracker() {}
}
