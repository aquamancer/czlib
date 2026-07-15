package com.aquamancer.czlib.internal;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.Screen;

import java.util.EnumMap;

public class ScreenCanceler {
    public enum Type { TRINKET, VZC }
    private static final int TICKS_UNTIL_TIMEOUT = 40;
    static {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> onTick());
    }

    private static int ticksUntilTimeout = TICKS_UNTIL_TIMEOUT;
    private static EnumMap<Type, Integer> screensToCancel = new EnumMap<>(Type.class);

    public static boolean shouldCancelScreen(Screen screen) {
        String name = screen.getTitle().getString();
        if (name.endsWith("Abilities")) {
            Integer newValue = screensToCancel.computeIfPresent(Type.TRINKET, (k, v) -> {
                if (v <= 0) {
                    return null;
                } else {
                    return v - 1;
                }
            });
            return newValue != null;
        } else if (name.endsWith("Charms")) {
            Integer newValue = screensToCancel.computeIfPresent(Type.VZC, (k, v) -> {
                if (v <= 0) {
                    return null;
                } else {
                    return v - 1;
                }
            });
            return newValue != null;
        } else {
            return false;
        }
    }

    static void cancelFutureScreens(int count, Type type) {
        screensToCancel.put(type, count);
        ticksUntilTimeout = TICKS_UNTIL_TIMEOUT;
    }

    private static void onTick() {
        if (ticksUntilTimeout == 0) {
            screensToCancel.clear();
        } else if (ticksUntilTimeout > 0) {
            ticksUntilTimeout--;
        }
    }
}
