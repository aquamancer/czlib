package com.aquamancer.czlib.api.abils;

import java.util.*;

public enum AbilitySpec {
    PRISMATIC,
    DAWN,
    EARTH,
    FLAME,
    FROST,
    SHADOW,
    STEEL,
    WIND;

    private static Map<String, AbilitySpec> fromString = new HashMap<>();
    private static Map<String, AbilitySpec> fromAbilityName = new HashMap<>();

    public static Optional<AbilitySpec> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
    }

    public static Optional<AbilitySpec> fromAbilityName(String ability) {
        return Optional.ofNullable(fromAbilityName.get(ability));
    }

    static {
        fromString.put("Prismatic", PRISMATIC);
        fromString.put("Dawnbringer", DAWN);
        fromString.put("Earthbound", EARTH);
        fromString.put("Flamecaller", FLAME);
        fromString.put("Frostborn", FROST);
        fromString.put("Shadowdancer", SHADOW);
        fromString.put("Steelsage", STEEL);
        fromString.put("Windwalker", WIND);
    }

    static {
        fromAbilityName.put("Soothing Combos", AbilitySpec.DAWN);
        fromAbilityName.put("Ward of Light", AbilitySpec.DAWN);
        fromAbilityName.put("Radiant Blessing", AbilitySpec.DAWN);
        fromAbilityName.put("Bottled Sunlight", AbilitySpec.DAWN);
        fromAbilityName.put("Lightning Bottle", AbilitySpec.DAWN);
        fromAbilityName.put("Divine Beam", AbilitySpec.DAWN);
        fromAbilityName.put("Spark of Inspiration", AbilitySpec.DAWN);
        fromAbilityName.put("Eternal Savior", AbilitySpec.DAWN);
        fromAbilityName.put("Enlightenment", AbilitySpec.DAWN);
        fromAbilityName.put("Rejuvenation", AbilitySpec.DAWN);
        fromAbilityName.put("Sundrops", AbilitySpec.DAWN);

        fromAbilityName.put("Earthen Combos", AbilitySpec.EARTH);
        fromAbilityName.put("Beast's Claw", AbilitySpec.EARTH);
        fromAbilityName.put("Taunt", AbilitySpec.EARTH);
        fromAbilityName.put("Iron Grip", AbilitySpec.EARTH);
        fromAbilityName.put("Entrench", AbilitySpec.EARTH);
        fromAbilityName.put("Earthquake", AbilitySpec.EARTH);
        fromAbilityName.put("Earthen Wrath", AbilitySpec.EARTH);
        fromAbilityName.put("Bramble Shell", AbilitySpec.EARTH);
        fromAbilityName.put("Bulwark", AbilitySpec.EARTH);
        fromAbilityName.put("Toughness", AbilitySpec.EARTH);

        fromAbilityName.put("Volcanic Combos", AbilitySpec.FLAME);
        fromAbilityName.put("Fireball", AbilitySpec.FLAME);
        fromAbilityName.put("Igneous Rune", AbilitySpec.FLAME);
        fromAbilityName.put("Flamestrike", AbilitySpec.FLAME);
        fromAbilityName.put("Flame Spirit", AbilitySpec.FLAME);
        fromAbilityName.put("Pyroblast", AbilitySpec.FLAME);
        fromAbilityName.put("Volcanic Meteor", AbilitySpec.FLAME);
        fromAbilityName.put("Apocalypse", AbilitySpec.FLAME);
        fromAbilityName.put("Detonation", AbilitySpec.FLAME);
        fromAbilityName.put("Primordial Mastery", AbilitySpec.FLAME);
        fromAbilityName.put("Pyromania", AbilitySpec.FLAME);

        fromAbilityName.put("Frigid Combos", AbilitySpec.FROST);
        fromAbilityName.put("Ice Lance", AbilitySpec.FROST);
        fromAbilityName.put("Snowstorm", AbilitySpec.FROST);
        fromAbilityName.put("Ice Barrier", AbilitySpec.FROST);
        fromAbilityName.put("Permafrost", AbilitySpec.FROST);
        fromAbilityName.put("Piercing Cold", AbilitySpec.FROST);
        fromAbilityName.put("Avalanche", AbilitySpec.FROST);
        fromAbilityName.put("Cryobox", AbilitySpec.FROST);
        fromAbilityName.put("Frozen Domain", AbilitySpec.FROST);
        fromAbilityName.put("Icebreaker", AbilitySpec.FROST);

        fromAbilityName.put("Dark Combos", AbilitySpec.SHADOW);
        fromAbilityName.put("Advancing Shadows", AbilitySpec.SHADOW);
        fromAbilityName.put("Cloak of Shadows", AbilitySpec.SHADOW);
        fromAbilityName.put("Blade Flurry", AbilitySpec.SHADOW);
        fromAbilityName.put("Phantom Force", AbilitySpec.SHADOW);
        fromAbilityName.put("Dummy Decoy", AbilitySpec.SHADOW);
        fromAbilityName.put("Chaos Dagger", AbilitySpec.SHADOW);
        fromAbilityName.put("Escape Artist", AbilitySpec.SHADOW);
        fromAbilityName.put("Brutalize", AbilitySpec.SHADOW);
        fromAbilityName.put("Deadly Strike", AbilitySpec.SHADOW);
        fromAbilityName.put("Dethroner", AbilitySpec.SHADOW);
        fromAbilityName.put("Shadow Slam", AbilitySpec.SHADOW);

        fromAbilityName.put("Focused Combos", AbilitySpec.STEEL);
        fromAbilityName.put("Sidearm", AbilitySpec.STEEL);
        fromAbilityName.put("Scrapshot", AbilitySpec.STEEL);
        fromAbilityName.put("Firework Blast", AbilitySpec.STEEL);
        fromAbilityName.put("Rapid Fire", AbilitySpec.STEEL);
        fromAbilityName.put("Volley", AbilitySpec.STEEL);
        fromAbilityName.put("Gravity Bomb", AbilitySpec.STEEL);
        fromAbilityName.put("Steel Stallion", AbilitySpec.STEEL);
        fromAbilityName.put("Sharpshooter", AbilitySpec.STEEL);
        fromAbilityName.put("Split Arrow", AbilitySpec.STEEL);

        fromAbilityName.put("Windswept Combos", AbilitySpec.WIND);
        fromAbilityName.put("Wind Walk", AbilitySpec.WIND);
        fromAbilityName.put("Guarding Bolt", AbilitySpec.WIND);
        fromAbilityName.put("Aeroblast", AbilitySpec.WIND);
        fromAbilityName.put("Whirlwind", AbilitySpec.WIND);
        fromAbilityName.put("Skyhook", AbilitySpec.WIND);
        fromAbilityName.put("Thundercloud Form", AbilitySpec.WIND);
        fromAbilityName.put("Last Breath", AbilitySpec.WIND);
        fromAbilityName.put("Aeromancy", AbilitySpec.WIND);
        fromAbilityName.put("Dodging", AbilitySpec.WIND);
        fromAbilityName.put("One with the Wind", AbilitySpec.WIND);
        fromAbilityName.put("Restoring Draft", AbilitySpec.WIND);

        fromAbilityName.put("Chroma Blade", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Solar Ray", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Encore", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Disco Ball", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Convergence", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Refraction", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Color Splash", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Abnormality", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Generosity", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Charity", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Flexibility", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Multiplicity", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Prosperity", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Rebirth", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Diversity", AbilitySpec.PRISMATIC);
        fromAbilityName.put("Opportunity", AbilitySpec.PRISMATIC);
    }

    public static class SpecComparator implements Comparator<HasAbilitySpec> {
        private final Map<AbilitySpec, Integer> priority;
        public SpecComparator(Map<AbilitySpec, Integer> priority) {
            this.priority = priority;
        }

        @Override
        public int compare(HasAbilitySpec o1, HasAbilitySpec o2) {
            Integer p1 = priority.get(o1.getSpec());
            Integer p2 = priority.get(o2.getSpec());
            if (p1 == null && p2 == null) return 0;
            if (p1 == null) return -1;
            if (p2 == null) return 1;
            return p1 - p2;
        }
    }
}
