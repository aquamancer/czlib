package com.aquamancer.czlib.api.abils;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;


public enum ActiveSlot {
    COMBO("Combo"),
    RIGHT("Right"),
    LEFT_SHIFT("Left Shift"),
    RIGHT_SHIFT("Right Shift"),
    WILDCARD("Wildcard"),
    BOW("Bow"),
    SWAP("Swap"),
    LIFELINE("Lifeline");

    private final String displayName;

    ActiveSlot(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
