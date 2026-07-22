package com.aquamancer.czlib.api.abils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Passives {
    ENLIGHTENMENT("Enlightenment"),
    REJUVENATION("Rejuvenation"),
    SUNDROPS("Sundrops"),
    BRAMBLE_SHELL("Bramble Shell"),
    BULWARK("Bulwark"),
    TOUGHNESS("Toughness"),
    DETONATION("Detonation"),
    PRIMORDIAL_MASTERY("Primordial Mastery"),
    PYROMANIA("Pyromania"),
    FROZEN_DOMAIN("Frozen Domain"),
    ICEBREAKER("Icebreaker"),
    BRUTALIZE("Brutalize"),
    DEADLY_STRIKE("Deadly Strike"),
    DETHRONER("Dethroner"),
    SHADOW_SLAM("Shadow Slam"),
    SHARPSHOOTER("Sharpshooter"),
    SPLIT_ARROW("Split Arrow"),
    AEROMANCY("Aeromancy"),
    DODGING("Dodging"),
    ONE_WITH_THE_WIND("One with the Wind"),
    RESTORING_DRAFT("Restoring Draft"),
    ABNORMALITY("Abnormality"),
    GENEROSITY("Generosity"),
    CHARITY("Charity"),
    FLEXIBILITY("Flexibility"),
    MULTIPLICITY("Multiplicity"),
    PROSPERITY("Prosperity"),
    REBIRTH("Rebirth"),
    DIVERSITY("Diversity"),
    OPPORTUNITY("Opportunity");

    private final String displayName;

    Passives(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final Map<String, Passives> FROM_STRING = Arrays.stream(values())
            .collect(Collectors.toMap(Passives::getDisplayName, Function.identity()));

    public static Optional<Passives> fromString(String string) {
        return Optional.ofNullable(FROM_STRING.get(string));
    }
}
