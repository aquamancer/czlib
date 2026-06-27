package com.aquamancer.czlib.api.party;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Curse {
    ANCHORING,
    ARACHNOPHOBIA,
    CHAOS,
    DEATH,
    DEPENDENCY,
    ENVY,
    GLUTTONY,
    GREED,
    IMPATIENCE,
    LUST,
    OBSCURITY,
    PESSIMISM,
    PRIDE,
    REDUNDANCY,
    RUIN,
    SLOTH,
    SOBRIETY;

    private static final Map<String, Curse> fromString = new HashMap<>();

    public static Optional<Curse> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
    }

    static {
        fromString.put("Curse of Anchoring", ANCHORING);
        fromString.put("Curse of Arachnophobia", ARACHNOPHOBIA);
        fromString.put("Curse of Chaos", CHAOS);
        fromString.put("Curse of Death", DEATH);
        fromString.put("Curse of Dependency", DEPENDENCY);
        fromString.put("Curse of Envy", ENVY);
        fromString.put("Curse of Gluttony", GLUTTONY);
        fromString.put("Curse of Greed", GREED);
        fromString.put("Curse of Impatience", IMPATIENCE);
        fromString.put("Curse of Lust", LUST);
        fromString.put("Curse of Obscurity", OBSCURITY);
        fromString.put("Curse of Pessimism", PESSIMISM);
        fromString.put("Curse of Pride", PRIDE);
        fromString.put("Curse of Redundancy", REDUNDANCY);
        fromString.put("Curse of Ruin", RUIN);
        fromString.put("Curse of Sloth", SLOTH);
        fromString.put("Curse of Sobriety", SOBRIETY);
    }
}
