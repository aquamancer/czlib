package com.aquamancer.czlib.api.abils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Actives {
    // Combo
    SOOTHING("Soothing Combos", ActiveSlot.COMBO),
    EARTHEN("Earthen Combos", ActiveSlot.COMBO),
    VOLCANIC("Volcanic Combos", ActiveSlot.COMBO),
    FRIGID("Frigid Combos", ActiveSlot.COMBO),
    DARK("Dark Combos", ActiveSlot.COMBO),
    FOCUSED("Focused Combos", ActiveSlot.COMBO),
    WINDSWEPT("Windswept Combos", ActiveSlot.COMBO),

    // Right
    WARD_OF_LIGHT("Ward of Light", ActiveSlot.RIGHT),
    BEASTS_CLAW("Beast's Claw", ActiveSlot.RIGHT),
    FIREBALL("Fireball", ActiveSlot.RIGHT),
    ICE_LANCE("Ice Lance", ActiveSlot.RIGHT),
    ADVANCING_SHADOWS("Advancing Shadows", ActiveSlot.RIGHT),
    SIDEARM("Sidearm", ActiveSlot.RIGHT),
    WIND_WALK("Wind Walk", ActiveSlot.RIGHT),
    SOLAR_RAY("Solar Ray", ActiveSlot.RIGHT),

    // Left Shift
    RADIANT_BLESSING("Radiant Blessing", ActiveSlot.LEFT_SHIFT),
    TAUNT("Taunt", ActiveSlot.LEFT_SHIFT),
    IGNEOUS_RUNE("Igneous Rune", ActiveSlot.LEFT_SHIFT),
    SNOWSTORM("Snowstorm", ActiveSlot.LEFT_SHIFT),
    CLOAK_OF_SHADOWS("Cloak of Shadows", ActiveSlot.LEFT_SHIFT),
    SCRAPSHOT("Scrapshot", ActiveSlot.LEFT_SHIFT),
    GUARDING_BOLT("Guarding Bolt", ActiveSlot.LEFT_SHIFT),
    ENCORE("Encore", ActiveSlot.LEFT_SHIFT),

    // Right Shift
    BOTTLED_SUNLIGHT("Bottled Sunlight", ActiveSlot.RIGHT_SHIFT),
    IRON_GRIP("Iron Grip", ActiveSlot.RIGHT_SHIFT),
    FLAMESTRIKE("Flamestrike", ActiveSlot.RIGHT_SHIFT),
    ICE_BARRIER("Ice Barrier", ActiveSlot.RIGHT_SHIFT),
    BLADE_FLURRY("Blade Flurry", ActiveSlot.RIGHT_SHIFT),
    FIREWORK_BLAST("Firework Blast", ActiveSlot.RIGHT_SHIFT),
    AEROBLAST("Aeroblast", ActiveSlot.RIGHT_SHIFT),
    CHROMA_BLADE("Chroma Blade", ActiveSlot.RIGHT_SHIFT),

    // Wildcard
    LIGHTNING_BOTTLE("Lightning Bottle", ActiveSlot.WILDCARD),
    ENTRENCH("Entrench", ActiveSlot.WILDCARD),
    FLAME_SPIRIT("Flame Spirit", ActiveSlot.WILDCARD),
    PERMAFROST("Permafrost", ActiveSlot.WILDCARD),
    PHANTOM_FORCE("Phantom Force", ActiveSlot.WILDCARD),
    RAPID_FIRE("Rapid Fire", ActiveSlot.WILDCARD),
    WHIRLWIND("Whirlwind", ActiveSlot.WILDCARD),
    CONVERGENCE("Convergence", ActiveSlot.WILDCARD),

    // Bow
    DIVINE_BEAM("Divine Beam", ActiveSlot.BOW),
    EARTHQUAKE("Earthquake", ActiveSlot.BOW),
    PYROBLAST("Pyroblast", ActiveSlot.BOW),
    PIERCING_COLD("Piercing Cold", ActiveSlot.BOW),
    DUMMY_DECOY("Dummy Decoy", ActiveSlot.BOW),
    VOLLEY("Volley", ActiveSlot.BOW),
    SKYHOOK("Skyhook", ActiveSlot.BOW),
    DISCO_BALL("Disco Ball", ActiveSlot.BOW),

    // Swap
    SPARK_OF_INSPIRATION("Spark of Inspiration", ActiveSlot.SWAP),
    EARTHEN_WRATH("Earthen Wrath", ActiveSlot.SWAP),
    VOLCANIC_METEOR("Volcanic Meteor", ActiveSlot.SWAP),
    AVALANCHE("Avalanche", ActiveSlot.SWAP),
    CHAOS_DAGGER("Chaos Dagger", ActiveSlot.SWAP),
    GRAVITY_BOMB("Gravity Bomb", ActiveSlot.SWAP),
    THUNDERCLOUD_FORM("Thundercloud Form", ActiveSlot.SWAP),
    REFRACTION("Refraction", ActiveSlot.SWAP),
    COLOR_SPLASH("Color Splash", ActiveSlot.SWAP),

    // Lifeline
    ETERNAL_SAVIOR("Eternal Savior", ActiveSlot.LIFELINE),
    APOCALYPSE("Apocalypse", ActiveSlot.LIFELINE),
    CRYOBOX("Cryobox", ActiveSlot.LIFELINE),
    ESCAPE_ARTIST("Escape Artist", ActiveSlot.LIFELINE),
    STEEL_STALLION("Steel Stallion", ActiveSlot.LIFELINE),
    LAST_BREATH("Last Breath", ActiveSlot.LIFELINE);

    private final String displayName;
    private final ActiveSlot slot;

    Actives(String displayName, ActiveSlot slot) {
        this.displayName = displayName;
        this.slot = slot;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ActiveSlot getSlot() {
        return slot;
    }

    private static final Map<String, Actives> FROM_STRING =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            Actives::getDisplayName,
                            Function.identity()
                    ));

    public static Optional<Actives> fromString(String name) {
        return Optional.ofNullable(FROM_STRING.get(name));
    }
}
