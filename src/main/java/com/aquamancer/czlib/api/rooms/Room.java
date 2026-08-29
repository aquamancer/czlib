package com.aquamancer.czlib.api.rooms;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Room {
    ABILITY,
    ABILITY_ELITE,
    UPGRADE,
    UPGRADE_ELITE,
    UTILITY,
    BOSS,
    // custom
    TREE_SELECT,
    ABILITY_SELECT,
    BOSS_CLEANSE,
    PRE_FLOOR;

    private static Map<String, Room> fromString = new HashMap<>();

    public static Optional<Room> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
    }

    static {
        fromString.put("Ability", ABILITY);
        fromString.put("Elite Ability", ABILITY_ELITE);
        fromString.put("Upgrade", UPGRADE);
        fromString.put("Elite Upgrade", UPGRADE_ELITE);
        fromString.put("Utility", UTILITY);
        fromString.put("Boss", BOSS);
    }
}