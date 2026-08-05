package com.aquamancer.czlib.api.abils;

import java.util.EnumSet;

public enum ActiveSlot {
    COMBO(EnumSet.of(
            Actives.SOOTHING,
            Actives.EARTHEN,
            Actives.VOLCANIC,
            Actives.FRIGID,
            Actives.DARK,
            Actives.FOCUSED,
            Actives.WINDSWEPT
    )),
    RIGHT(EnumSet.of(
            Actives.WARD_OF_LIGHT,
            Actives.BEASTS_CLAW,
            Actives.FIREBALL,
            Actives.ICE_LANCE,
            Actives.ADVANCING_SHADOWS,
            Actives.SIDEARM,
            Actives.WIND_WALK,
            Actives.SOLAR_RAY
    )),
    LEFT_SHIFT(EnumSet.of(
            Actives.RADIANT_BLESSING,
            Actives.TAUNT,
            Actives.IGNEOUS_RUNE,
            Actives.SNOWSTORM,
            Actives.CLOAK_OF_SHADOWS,
            Actives.SCRAPSHOT,
            Actives.GUARDING_BOLT,
            Actives.ENCORE
    )),
    RIGHT_SHIFT(EnumSet.of(
            Actives.BOTTLED_SUNLIGHT,
            Actives.IRON_GRIP,
            Actives.FLAMESTRIKE,
            Actives.ICE_BARRIER,
            Actives.BLADE_FLURRY,
            Actives.FIREWORK_BLAST,
            Actives.AEROBLAST,
            Actives.CHROMA_BLADE
    )),
    WILDCARD(EnumSet.of(
            Actives.LIGHTNING_BOTTLE,
            Actives.ENTRENCH,
            Actives.FLAME_SPIRIT,
            Actives.PERMAFROST,
            Actives.PHANTOM_FORCE,
            Actives.RAPID_FIRE,
            Actives.WHIRLWIND,
            Actives.CONVERGENCE
    )),
    BOW(EnumSet.of(
            Actives.DIVINE_BEAM,
            Actives.EARTHQUAKE,
            Actives.PYROBLAST,
            Actives.PIERCING_COLD,
            Actives.DUMMY_DECOY,
            Actives.VOLLEY,
            Actives.SKYHOOK,
            Actives.DISCO_BALL
    )),
    SWAP(EnumSet.of(
            Actives.SPARK_OF_INSPIRATION,
            Actives.EARTHEN_WRATH,
            Actives.VOLCANIC_METEOR,
            Actives.AVALANCHE,
            Actives.CHAOS_DAGGER,
            Actives.GRAVITY_BOMB,
            Actives.THUNDERCLOUD_FORM,
            Actives.REFRACTION,
            Actives.COLOR_SPLASH
    )),
    LIFELINE(EnumSet.of(
            Actives.ETERNAL_SAVIOR,
            Actives.APOCALYPSE,
            Actives.CRYOBOX,
            Actives.ESCAPE_ARTIST,
            Actives.STEEL_STALLION,
            Actives.LAST_BREATH
    ));

    private final EnumSet<Actives> actives;

    ActiveSlot(EnumSet<Actives> actives) {
        this.actives = actives;
    }

    public EnumSet<Actives> getActives() {
        return EnumSet.copyOf(actives);
    }
}
