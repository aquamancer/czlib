package com.aquamancer.czlib.api.abils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    TWISTED;

    private static final Map<String, Rarity> fromString = new HashMap<>();
    static {
        fromString.put("Common", COMMON);
        fromString.put("Uncommon", UNCOMMON);
        fromString.put("Rare", RARE);
        fromString.put("Epic", EPIC);
        fromString.put("Legendary", LEGENDARY);
        fromString.put("XXXXXX", TWISTED);
    }
    public static Optional<Rarity> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
    }

    public static Rarity downgrade(Rarity rarity) {
        return switch (rarity) {
            case TWISTED -> TWISTED;
            case LEGENDARY -> EPIC;
            case EPIC -> RARE;
            case RARE -> UNCOMMON;
            case UNCOMMON, COMMON -> COMMON;
        };
    }
}
