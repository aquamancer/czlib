package com.aquamancer.czlib.api.abils;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public enum ActiveSlot {
    COMBO(new EnumMap<>(Map.of(
            AbilitySpec.DAWN, EnumSet.of(Actives.SOOTHING),
            AbilitySpec.EARTH, EnumSet.of(Actives.EARTHEN),
            AbilitySpec.FLAME, EnumSet.of(Actives.VOLCANIC),
            AbilitySpec.FROST, EnumSet.of(Actives.FRIGID),
            AbilitySpec.SHADOW, EnumSet.of(Actives.DARK),
            AbilitySpec.STEEL, EnumSet.of(Actives.FOCUSED),
            AbilitySpec.WIND, EnumSet.of(Actives.WINDSWEPT)
    ))),
    RIGHT(new EnumMap<>(Map.of(
            AbilitySpec.DAWN, EnumSet.of(Actives.WARD_OF_LIGHT),
            AbilitySpec.EARTH, EnumSet.of(Actives.BEASTS_CLAW),
            AbilitySpec.FLAME, EnumSet.of(Actives.FIREBALL),
            AbilitySpec.FROST, EnumSet.of(Actives.ICE_LANCE),
            AbilitySpec.SHADOW, EnumSet.of(Actives.ADVANCING_SHADOWS),
            AbilitySpec.STEEL, EnumSet.of(Actives.SIDEARM),
            AbilitySpec.WIND, EnumSet.of(Actives.WIND_WALK),
            AbilitySpec.PRISMATIC, EnumSet.of(Actives.SOLAR_RAY)
    ))),

    LEFT_SHIFT(new EnumMap<>(Map.of(
            AbilitySpec.DAWN, EnumSet.of(Actives.RADIANT_BLESSING),
            AbilitySpec.EARTH, EnumSet.of(Actives.TAUNT),
            AbilitySpec.FLAME, EnumSet.of(Actives.IGNEOUS_RUNE),
            AbilitySpec.FROST, EnumSet.of(Actives.SNOWSTORM),
            AbilitySpec.SHADOW, EnumSet.of(Actives.CLOAK_OF_SHADOWS),
            AbilitySpec.STEEL, EnumSet.of(Actives.SCRAPSHOT),
            AbilitySpec.WIND, EnumSet.of(Actives.GUARDING_BOLT),
            AbilitySpec.PRISMATIC, EnumSet.of(Actives.ENCORE)
    ))),

    RIGHT_SHIFT(new EnumMap<>(Map.of(
            AbilitySpec.DAWN, EnumSet.of(Actives.BOTTLED_SUNLIGHT),
            AbilitySpec.EARTH, EnumSet.of(Actives.IRON_GRIP),
            AbilitySpec.FLAME, EnumSet.of(Actives.FLAMESTRIKE),
            AbilitySpec.FROST, EnumSet.of(Actives.ICE_BARRIER),
            AbilitySpec.SHADOW, EnumSet.of(Actives.BLADE_FLURRY),
            AbilitySpec.STEEL, EnumSet.of(Actives.FIREWORK_BLAST),
            AbilitySpec.WIND, EnumSet.of(Actives.AEROBLAST),
            AbilitySpec.PRISMATIC, EnumSet.of(Actives.CHROMA_BLADE)
    ))),

    WILDCARD(new EnumMap<>(Map.of(
            AbilitySpec.DAWN, EnumSet.of(Actives.LIGHTNING_BOTTLE),
            AbilitySpec.EARTH, EnumSet.of(Actives.ENTRENCH),
            AbilitySpec.FLAME, EnumSet.of(Actives.FLAME_SPIRIT),
            AbilitySpec.FROST, EnumSet.of(Actives.PERMAFROST),
            AbilitySpec.SHADOW, EnumSet.of(Actives.PHANTOM_FORCE),
            AbilitySpec.STEEL, EnumSet.of(Actives.RAPID_FIRE),
            AbilitySpec.WIND, EnumSet.of(Actives.WHIRLWIND),
            AbilitySpec.PRISMATIC, EnumSet.of(Actives.CONVERGENCE)
    ))),

    BOW(new EnumMap<>(Map.of(
            AbilitySpec.DAWN, EnumSet.of(Actives.DIVINE_BEAM),
            AbilitySpec.EARTH, EnumSet.of(Actives.EARTHQUAKE),
            AbilitySpec.FLAME, EnumSet.of(Actives.PYROBLAST),
            AbilitySpec.FROST, EnumSet.of(Actives.PIERCING_COLD),
            AbilitySpec.SHADOW, EnumSet.of(Actives.DUMMY_DECOY),
            AbilitySpec.STEEL, EnumSet.of(Actives.VOLLEY),
            AbilitySpec.WIND, EnumSet.of(Actives.SKYHOOK),
            AbilitySpec.PRISMATIC, EnumSet.of(Actives.DISCO_BALL)
    ))),

    SWAP(new EnumMap<>(Map.of(
            AbilitySpec.DAWN, EnumSet.of(Actives.SPARK_OF_INSPIRATION),
            AbilitySpec.EARTH, EnumSet.of(Actives.EARTHEN_WRATH),
            AbilitySpec.FLAME, EnumSet.of(Actives.VOLCANIC_METEOR),
            AbilitySpec.FROST, EnumSet.of(Actives.AVALANCHE),
            AbilitySpec.SHADOW, EnumSet.of(Actives.CHAOS_DAGGER),
            AbilitySpec.STEEL, EnumSet.of(Actives.GRAVITY_BOMB),
            AbilitySpec.WIND, EnumSet.of(Actives.THUNDERCLOUD_FORM),
            AbilitySpec.PRISMATIC, EnumSet.of(Actives.REFRACTION, Actives.COLOR_SPLASH)
    ))),

    LIFELINE(new EnumMap<>(Map.of(
            AbilitySpec.DAWN, EnumSet.of(Actives.ETERNAL_SAVIOR),
            AbilitySpec.FLAME, EnumSet.of(Actives.APOCALYPSE),
            AbilitySpec.FROST, EnumSet.of(Actives.CRYOBOX),
            AbilitySpec.SHADOW, EnumSet.of(Actives.ESCAPE_ARTIST),
            AbilitySpec.STEEL, EnumSet.of(Actives.STEEL_STALLION),
            AbilitySpec.WIND, EnumSet.of(Actives.LAST_BREATH)
    )));

    private final Map<AbilitySpec, EnumSet<Actives>> actives;

    ActiveSlot(Map<AbilitySpec, EnumSet<Actives>> actives) {
        this.actives = actives;
    }

    public Map<AbilitySpec, EnumSet<Actives>> getActives() {
        return this.actives;
    }
}
