package com.aquamancer.czlib.api.abils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Actives {
    // Combo
    SOOTHING("Soothing Combos", ActiveSlot.COMBO, AbilitySpec.DAWN),
    EARTHEN("Earthen Combos", ActiveSlot.COMBO, AbilitySpec.EARTH),
    VOLCANIC("Volcanic Combos", ActiveSlot.COMBO, AbilitySpec.FLAME),
    FRIGID("Frigid Combos", ActiveSlot.COMBO, AbilitySpec.FROST),
    DARK("Dark Combos", ActiveSlot.COMBO, AbilitySpec.SHADOW),
    FOCUSED("Focused Combos", ActiveSlot.COMBO, AbilitySpec.STEEL),
    WINDSWEPT("Windswept Combos", ActiveSlot.COMBO, AbilitySpec.WIND),

    // Right
    WARD_OF_LIGHT("Ward of Light", ActiveSlot.RIGHT, AbilitySpec.DAWN),
    BEASTS_CLAW("Beast's Claw", ActiveSlot.RIGHT, AbilitySpec.EARTH),
    FIREBALL("Fireball", ActiveSlot.RIGHT, AbilitySpec.FLAME),
    ICE_LANCE("Ice Lance", ActiveSlot.RIGHT, AbilitySpec.FROST),
    ADVANCING_SHADOWS("Advancing Shadows", ActiveSlot.RIGHT, AbilitySpec.SHADOW),
    SIDEARM("Sidearm", ActiveSlot.RIGHT, AbilitySpec.STEEL),
    WIND_WALK("Wind Walk", ActiveSlot.RIGHT, AbilitySpec.WIND),
    SOLAR_RAY("Solar Ray", ActiveSlot.RIGHT, AbilitySpec.PRISMATIC),

    // Left Shift
    RADIANT_BLESSING("Radiant Blessing", ActiveSlot.LEFT_SHIFT, AbilitySpec.DAWN),
    TAUNT("Taunt", ActiveSlot.LEFT_SHIFT, AbilitySpec.EARTH),
    IGNEOUS_RUNE("Igneous Rune", ActiveSlot.LEFT_SHIFT, AbilitySpec.FLAME),
    SNOWSTORM("Snowstorm", ActiveSlot.LEFT_SHIFT, AbilitySpec.FROST),
    CLOAK_OF_SHADOWS("Cloak of Shadows", ActiveSlot.LEFT_SHIFT, AbilitySpec.SHADOW),
    SCRAPSHOT("Scrapshot", ActiveSlot.LEFT_SHIFT, AbilitySpec.STEEL),
    GUARDING_BOLT("Guarding Bolt", ActiveSlot.LEFT_SHIFT, AbilitySpec.WIND),
    ENCORE("Encore", ActiveSlot.LEFT_SHIFT, AbilitySpec.PRISMATIC),

    // Right Shift
    BOTTLED_SUNLIGHT("Bottled Sunlight", ActiveSlot.RIGHT_SHIFT, AbilitySpec.DAWN),
    IRON_GRIP("Iron Grip", ActiveSlot.RIGHT_SHIFT, AbilitySpec.EARTH),
    FLAMESTRIKE("Flamestrike", ActiveSlot.RIGHT_SHIFT, AbilitySpec.FLAME),
    ICE_BARRIER("Ice Barrier", ActiveSlot.RIGHT_SHIFT, AbilitySpec.FROST),
    BLADE_FLURRY("Blade Flurry", ActiveSlot.RIGHT_SHIFT, AbilitySpec.SHADOW),
    FIREWORK_BLAST("Firework Blast", ActiveSlot.RIGHT_SHIFT, AbilitySpec.STEEL),
    AEROBLAST("Aeroblast", ActiveSlot.RIGHT_SHIFT, AbilitySpec.WIND),
    CHROMA_BLADE("Chroma Blade", ActiveSlot.RIGHT_SHIFT, AbilitySpec.PRISMATIC),

    // Wildcard
    LIGHTNING_BOTTLE("Lightning Bottle", ActiveSlot.WILDCARD, AbilitySpec.DAWN),
    ENTRENCH("Entrench", ActiveSlot.WILDCARD, AbilitySpec.EARTH),
    FLAME_SPIRIT("Flame Spirit", ActiveSlot.WILDCARD, AbilitySpec.FLAME),
    PERMAFROST("Permafrost", ActiveSlot.WILDCARD, AbilitySpec.FROST),
    PHANTOM_FORCE("Phantom Force", ActiveSlot.WILDCARD, AbilitySpec.SHADOW),
    RAPID_FIRE("Rapid Fire", ActiveSlot.WILDCARD, AbilitySpec.STEEL),
    WHIRLWIND("Whirlwind", ActiveSlot.WILDCARD, AbilitySpec.WIND),
    CONVERGENCE("Convergence", ActiveSlot.WILDCARD, AbilitySpec.PRISMATIC),

    // Bow
    DIVINE_BEAM("Divine Beam", ActiveSlot.BOW, AbilitySpec.DAWN),
    EARTHQUAKE("Earthquake", ActiveSlot.BOW, AbilitySpec.EARTH),
    PYROBLAST("Pyroblast", ActiveSlot.BOW, AbilitySpec.FLAME),
    PIERCING_COLD("Piercing Cold", ActiveSlot.BOW, AbilitySpec.FROST),
    DUMMY_DECOY("Dummy Decoy", ActiveSlot.BOW, AbilitySpec.SHADOW),
    VOLLEY("Volley", ActiveSlot.BOW, AbilitySpec.STEEL),
    SKYHOOK("Skyhook", ActiveSlot.BOW, AbilitySpec.WIND),
    DISCO_BALL("Disco Ball", ActiveSlot.BOW, AbilitySpec.PRISMATIC),

    // Swap
    SPARK_OF_INSPIRATION("Spark of Inspiration", ActiveSlot.SWAP, AbilitySpec.DAWN),
    EARTHEN_WRATH("Earthen Wrath", ActiveSlot.SWAP, AbilitySpec.EARTH),
    VOLCANIC_METEOR("Volcanic Meteor", ActiveSlot.SWAP, AbilitySpec.FLAME),
    AVALANCHE("Avalanche", ActiveSlot.SWAP, AbilitySpec.FROST),
    CHAOS_DAGGER("Chaos Dagger", ActiveSlot.SWAP, AbilitySpec.SHADOW),
    GRAVITY_BOMB("Gravity Bomb", ActiveSlot.SWAP, AbilitySpec.STEEL),
    THUNDERCLOUD_FORM("Thundercloud Form", ActiveSlot.SWAP, AbilitySpec.WIND),
    REFRACTION("Refraction", ActiveSlot.SWAP, AbilitySpec.PRISMATIC),
    COLOR_SPLASH("Color Splash", ActiveSlot.SWAP, AbilitySpec.PRISMATIC),

    // Lifeline
    ETERNAL_SAVIOR("Eternal Savior", ActiveSlot.LIFELINE, AbilitySpec.DAWN),
    APOCALYPSE("Apocalypse", ActiveSlot.LIFELINE, AbilitySpec.FLAME),
    CRYOBOX("Cryobox", ActiveSlot.LIFELINE, AbilitySpec.FROST),
    ESCAPE_ARTIST("Escape Artist", ActiveSlot.LIFELINE, AbilitySpec.SHADOW),
    STEEL_STALLION("Steel Stallion", ActiveSlot.LIFELINE, AbilitySpec.STEEL),
    LAST_BREATH("Last Breath", ActiveSlot.LIFELINE, AbilitySpec.WIND);

    private final String displayName;
    private final ActiveSlot slot;
    private final AbilitySpec spec;

    Actives(String displayName, ActiveSlot slot, AbilitySpec spec) {
        this.displayName = displayName;
        this.slot = slot;
        this.spec = spec;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ActiveSlot getSlot() {
        return slot;
    }

    public AbilitySpec getSpec() {
        return spec;
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

    public static Optional<AbilitySpec> getSpec(String string) {
        return fromString(string).map(Actives::getSpec);
    }

    public static class ActiveSlotComparator implements Comparator<Actives> {
        public final Map<ActiveSlot, Integer> priority;
        public ActiveSlotComparator(Map<ActiveSlot, Integer> priority) {
            this.priority = priority;
        }

        @Override
        public int compare(Actives o1, Actives o2) {
            return priority.get(o1.getSlot()) - priority.get(o2.getSlot());
        }
    }

    public static class ActiveSlotComparator2 implements Comparator<Active> {
        public final Map<ActiveSlot, Integer> priority;
        public ActiveSlotComparator2(Map<ActiveSlot, Integer> priority) {
            this.priority = priority;
        }

        @Override
        public int compare(Active o1, Active o2) {
            Actives a1 = o1.getAbility();
            Actives a2 = o2.getAbility();
            if (a1 == null && a2 == null) return 0;
            if (a1 == null) return -1;
            if (a2 == null) return 1;

            return priority.get(a1.getSlot()) - priority.get(a2.getSlot());
        }
    }
}
