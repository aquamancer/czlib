package com.aquamancer.czlib.api.abils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Spec {
    DAWN,
    EARTH,
    FLAME,
    FROST,
    SHADOW,
    STEEL,
    WIND;

    private static Map<String, Spec> fromString = new HashMap<>();

    public static Optional<Spec> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
    }

    static {
        fromString.put("Dawnbringer", DAWN);
        fromString.put("Earthbound", EARTH);
        fromString.put("Flamecaller", FLAME);
        fromString.put("Frostborn", FROST);
        fromString.put("Shadowdancer", SHADOW);
        fromString.put("Steelsage", STEEL);
        fromString.put("Windwalker", WIND);
    }
}
