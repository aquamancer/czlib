package com.aquamancer.czlib.api.abils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum AbilitySpec {
    PRISMATIC,
    DAWN,
    EARTH,
    FLAME,
    FROST,
    SHADOW,
    STEEL,
    WIND;

    private static Map<String, AbilitySpec> fromString = new HashMap<>();

    public static Optional<AbilitySpec> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
    }

    static {
        fromString.put("Prismatic", PRISMATIC);
        fromString.put("Dawnbringer", DAWN);
        fromString.put("Earthbound", EARTH);
        fromString.put("Flamecaller", FLAME);
        fromString.put("Frostborn", FROST);
        fromString.put("Shadowdancer", SHADOW);
        fromString.put("Steelsage", STEEL);
        fromString.put("Windwalker", WIND);
    }
}
