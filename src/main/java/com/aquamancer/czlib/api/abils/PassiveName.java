package com.aquamancer.czlib.api.abils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum PassiveName {
    ENLIGHTENMENT,
    REJUVENATION,
    SUNDROPS,
    BRAMBLE_SHELL,
    BULWARK,
    TOUGHNESS,
    DETONATION,
    PRIMORDIAL_MASTERY,
    PYROMANIA,
    FROZEN_DOMAIN,
    ICEBREAKER,
    BRUTALIZE,
    DEADLY_STRIKE,
    DETHRONER,
    SHADOW_SLAM,
    SHARPSHOOTER,
    SPLIT_ARROW,
    AEROMANCY,
    DODGING,
    ONE_WITH_THE_WIND,
    RESTORING_DRAFT,
    ABNORMALITY,
    GENEROSITY,
    CHARITY,
    FLEXIBILITY,
    MULTIPLICITY,
    PROSPERITY,
    REBIRTH,
    DIVERSITY,
    OPPORTUNITY;

    private static final Map<String, PassiveName> fromString = new HashMap<>();

    public static Optional<PassiveName> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
    }

    static {
        fromString.put("Enlightenment", ENLIGHTENMENT);
        fromString.put("Rejuvenation", REJUVENATION);
        fromString.put("Sundrops", SUNDROPS);
        fromString.put("Bramble Shell", BRAMBLE_SHELL);
        fromString.put("Bulwark", BULWARK);
        fromString.put("Toughness", TOUGHNESS);
        fromString.put("Detonation", DETONATION);
        fromString.put("Primordial Mastery", PRIMORDIAL_MASTERY);
        fromString.put("Pyromania", PYROMANIA);
        fromString.put("Frozen Domain", FROZEN_DOMAIN);
        fromString.put("Icebreaker", ICEBREAKER);
        fromString.put("Brutalize", BRUTALIZE);
        fromString.put("Deadly Strike", DEADLY_STRIKE);
        fromString.put("Dethroner", DETHRONER);
        fromString.put("Shadow Slam", SHADOW_SLAM);
        fromString.put("Sharpshooter", SHARPSHOOTER);
        fromString.put("Split Arrow", SPLIT_ARROW);
        fromString.put("Aeromancy", AEROMANCY);
        fromString.put("Dodging", DODGING);
        fromString.put("One with the Wind", ONE_WITH_THE_WIND);
        fromString.put("Restoring Draft", RESTORING_DRAFT);
        fromString.put("Abnormality", ABNORMALITY);
        fromString.put("Generosity", GENEROSITY);
        fromString.put("Charity", CHARITY);
        fromString.put("Flexibility", FLEXIBILITY);
        fromString.put("Multiplicity", MULTIPLICITY);
        fromString.put("Prosperity", PROSPERITY);
        fromString.put("Rebirth", REBIRTH);
        fromString.put("Diversity", DIVERSITY);
        fromString.put("Opportunity", OPPORTUNITY);
    }
}
