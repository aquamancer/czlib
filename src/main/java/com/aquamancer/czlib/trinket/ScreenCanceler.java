package com.aquamancer.czlib.trinket;

public class ScreenCanceler {
    private static int screensToCancel = 0;

    static void cancelFutureScreens(int count) {
        screensToCancel = count;
    }


}
