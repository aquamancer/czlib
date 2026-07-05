package com.aquamancer.czlib.trinket;

import net.minecraft.client.gui.screen.Screen;

public class ScreenCanceler {
    private static int screensToCancel = 0;

    public static boolean shouldCancelScreen(Screen screen) {
        String name = screen.getTitle().getString();
        if (screensToCancel <= 0) return false;
        if (!name.endsWith("Abilities")) return false;
        screensToCancel--;
        return true;
    }

    static void cancelFutureScreens(int count) {
        screensToCancel = count;
    }
}
