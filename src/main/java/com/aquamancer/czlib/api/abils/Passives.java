package com.aquamancer.czlib.api.abils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Passives implements Ability {
    ENLIGHTENMENT("Enlightenment", AbilitySpec.DAWN),
    REJUVENATION("Rejuvenation", AbilitySpec.DAWN),
    SUNDROPS("Sundrops", AbilitySpec.DAWN),

    BRAMBLE_SHELL("Bramble Shell", AbilitySpec.EARTH),
    BULWARK("Bulwark", AbilitySpec.EARTH),
    TOUGHNESS("Toughness", AbilitySpec.EARTH),

    DETONATION("Detonation", AbilitySpec.FLAME),
    PRIMORDIAL_MASTERY("Primordial Mastery", AbilitySpec.FLAME),
    PYROMANIA("Pyromania", AbilitySpec.FLAME),

    FROZEN_DOMAIN("Frozen Domain", AbilitySpec.FROST),
    ICEBREAKER("Icebreaker", AbilitySpec.FROST),

    BRUTALIZE("Brutalize", AbilitySpec.SHADOW),
    DEADLY_STRIKE("Deadly Strike", AbilitySpec.SHADOW),
    DETHRONER("Dethroner", AbilitySpec.SHADOW),
    SHADOW_SLAM("Shadow Slam", AbilitySpec.SHADOW),

    SHARPSHOOTER("Sharpshooter", AbilitySpec.STEEL),
    SPLIT_ARROW("Split Arrow", AbilitySpec.STEEL),

    AEROMANCY("Aeromancy", AbilitySpec.WIND),
    DODGING("Dodging", AbilitySpec.WIND),
    ONE_WITH_THE_WIND("One with the Wind", AbilitySpec.WIND),
    RESTORING_DRAFT("Restoring Draft", AbilitySpec.WIND),

    ABNORMALITY("Abnormality", AbilitySpec.PRISMATIC),
    GENEROSITY("Generosity", AbilitySpec.PRISMATIC),
    CHARITY("Charity", AbilitySpec.PRISMATIC),
    FLEXIBILITY("Flexibility", AbilitySpec.PRISMATIC),
    MULTIPLICITY("Multiplicity", AbilitySpec.PRISMATIC),
    PROSPERITY("Prosperity", AbilitySpec.PRISMATIC),
    REBIRTH("Rebirth", AbilitySpec.PRISMATIC),
    DIVERSITY("Diversity", AbilitySpec.PRISMATIC),
    OPPORTUNITY("Opportunity", AbilitySpec.PRISMATIC);

    private final String displayName;
    private final AbilitySpec spec;

    Passives(String displayName, AbilitySpec spec) {
        this.displayName = displayName;
        this.spec = spec;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public AbilitySpec getSpec() {
        return this.spec;
    }

    public int getColor() {
        return this.spec.getColor();
    }

    @Override
    public Enum<?> getAbility() {
        return this;
    }

    private static final Map<String, Passives> FROM_STRING = Arrays.stream(values())
            .collect(Collectors.toMap(Passives::getDisplayName, Function.identity()));

    public static Optional<Passives> fromString(String string) {
        return Optional.ofNullable(FROM_STRING.get(string));
    }

    public static Optional<AbilitySpec> getSpec(String string) {
        return fromString(string).map(Passives::getSpec);
    }
}
