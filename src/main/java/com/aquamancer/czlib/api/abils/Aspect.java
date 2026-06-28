package com.aquamancer.czlib.api.abils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Aspect {
    BOX,
    AXE,
    BOW,
    SCYTHE,
    SWORD,
    WAND;

    private static final Map<String, Aspect> fromString = new HashMap<>();
    static{
        fromString.put("Mystery Box", BOX);
        fromString.put("Aspect of the Axe", AXE);
        fromString.put("Aspect of the Bow", BOW);
        fromString.put("Aspect of the Scythe", SCYTHE);
        fromString.put("Aspect of the Sword", SWORD);
        fromString.put("Aspect of the Wand", WAND);
    }

    public static Optional<Aspect> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
    }
}
