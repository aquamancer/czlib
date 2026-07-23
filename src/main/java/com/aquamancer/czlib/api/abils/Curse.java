package com.aquamancer.czlib.api.abils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Curse {
    ANCHORING("Curse of Anchoring"),
    ARACHNOPHOBIA("Curse of Arachnophobia"),
    CHAOS("Curse of Chaos"),
    DEATH("Curse of Death"),
    DEPENDENCY("Curse of Dependency"),
    ENVY("Curse of Envy"),
    GLUTTONY("Curse of Gluttony"),
    GREED("Curse of Greed"),
    IMPATIENCE("Curse of Impatience"),
    LUST("Curse of Lust"),
    OBSCURITY("Curse of Obscurity"),
    PESSIMISM("Curse of Pessimism"),
    PRIDE("Curse of Pride"),
    REDUNDANCY("Curse of Redundancy"),
    RUIN("Curse of Ruin"),
    SLOTH("Curse of Sloth"),
    SOBRIETY("Curse of Sobriety");

    private final String displayName;

    Curse(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final Map<String, Curse> FROM_STRING =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Curse::getDisplayName, Function.identity()));

    public static Optional<Curse> fromString(String string) {
        return Optional.ofNullable(FROM_STRING.get(string));
    }
}
