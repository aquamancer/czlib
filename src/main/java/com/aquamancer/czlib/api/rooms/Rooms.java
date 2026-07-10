package com.aquamancer.czlib.api.rooms;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// changing this will affect Gift Treasure Map
public enum Rooms {
    ABILITY,
    ABILITY_ELITE,
    UPGRADE,
    UPGRADE_ELITE,
    UTILITY,
    BOSS;

    private static Map<String, Rooms> fromString = new HashMap<>();

    public static Optional<Rooms> toEnum(String string) {
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