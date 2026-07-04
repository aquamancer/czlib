package com.aquamancer.czlib.api.abils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Actives {
    public static Optional<? extends ActiveType> toEnum(String s) {
        Optional<Combo> c = Combo.toEnum(s);
        if (c.isPresent()) return c;
        Optional<Right> r = Right.toEnum(s);
        if (r.isPresent()) return r;
        Optional<LeftShift> ls = LeftShift.toEnum(s);
        if (ls.isPresent()) return ls;
        Optional<RightShift> rs = RightShift.toEnum(s);
        if (rs.isPresent()) return rs;
        Optional<Wildcard> wc = Wildcard.toEnum(s);
        if (wc.isPresent()) return wc;
        Optional<Bow> b = Bow.toEnum(s);
        if (b.isPresent()) return b ;
        Optional<Swap> swap = Swap.toEnum(s);
        if (swap.isPresent()) return swap;
        Optional<Lifeline> ll = Lifeline.toEnum(s);
        if (ll.isPresent()) return ll;

        return Optional.empty();
    }

    public enum Combo implements ActiveType {
        SOOTHING,
        EARTHEN,
        VOLCANIC,
        FRIGID,
        DARK,
        FOCUSED,
        WINDSWEPT;

        private static final Map<String, Combo> fromString = new HashMap<>();
        static {
            fromString.put("Soothing Combos", SOOTHING);
            fromString.put("Earthen Combos", EARTHEN);
            fromString.put("Volcanic Combos", VOLCANIC);
            fromString.put("Frigid Combos", FRIGID);
            fromString.put("Dark Combos", DARK);
            fromString.put("Focused Combos", FOCUSED);
            fromString.put("Windswept Combos", WINDSWEPT);
        }

        public static Optional<Combo> toEnum(String string) {
            return Optional.ofNullable(fromString.get(string));
        }

        @Override
        public ActiveSlot getSlot() {
            return ActiveSlot.COMBO;
        }
    }

    public enum Right implements ActiveType {
        WARD_OF_LIGHT,
        BEASTS_CLAW,
        FIREBALL,
        ICE_LANCE,
        ADVANCING_SHADOWS,
        SIDEARM,
        WIND_WALK,
        SOLAR_RAY;

        private static final Map<String, Right> fromString = new HashMap<>();
        static {
            fromString.put("Ward of Light", WARD_OF_LIGHT);
            fromString.put("Beast's Claw", BEASTS_CLAW);
            fromString.put("Fireball", FIREBALL);
            fromString.put("Ice Lance", ICE_LANCE);
            fromString.put("Advancing Shadows", ADVANCING_SHADOWS);
            fromString.put("Sidearm", SIDEARM);
            fromString.put("Wind Walk", WIND_WALK);
            fromString.put("Solar Ray", SOLAR_RAY);
        }

        public static Optional<Right> toEnum(String string) {
            return Optional.ofNullable(fromString.get(string));
        }

        @Override
        public ActiveSlot getSlot() {
            return ActiveSlot.RIGHT;
        }
    }

    public enum LeftShift implements ActiveType {
        RADIANT_BLESSING,
        TAUNT,
        IGNEOUS_RUNE,
        SNOWSTORM,
        CLOAK_OF_SHADOWS,
        SCRAPSHOT,
        GUARDING_BOLT,
        ENCORE;

        private static final Map<String, LeftShift> fromString = new HashMap<>();
        static {
            fromString.put("Radiant Blessing", RADIANT_BLESSING);
            fromString.put("Taunt", TAUNT);
            fromString.put("Igneous Rune", IGNEOUS_RUNE);
            fromString.put("Snowstorm", SNOWSTORM);
            fromString.put("Cloak of Shadows", CLOAK_OF_SHADOWS);
            fromString.put("Scrapshot", SCRAPSHOT);
            fromString.put("Guarding Bolt", GUARDING_BOLT);
            fromString.put("Encore", ENCORE);
        }

        public static Optional<LeftShift> toEnum(String string) {
            return Optional.ofNullable(fromString.get(string));
        }

        @Override
        public ActiveSlot getSlot() {
            return ActiveSlot.LEFT_SHIFT;
        }
    }

    public enum RightShift implements ActiveType {
        BOTTLED_SUNLIGHT,
        IRON_GRIP,
        FLAMESTRIKE,
        ICE_BARRIER,
        BLADE_FLURRY,
        FIREWORK_BLAST,
        AEROBLAST,
        CHROMA_BLADE;

        private static final Map<String, RightShift> fromString = new HashMap<>();
        static {
            fromString.put("Bottled Sunlight", BOTTLED_SUNLIGHT);
            fromString.put("Iron Grip", IRON_GRIP);
            fromString.put("Flamestrike", FLAMESTRIKE);
            fromString.put("Ice Barrier", ICE_BARRIER);
            fromString.put("Blade Flurry", BLADE_FLURRY);
            fromString.put("Firework Blast", FIREWORK_BLAST);
            fromString.put("Aeroblast", AEROBLAST);
            fromString.put("Chroma Blade", CHROMA_BLADE);
        }

        public static Optional<RightShift> toEnum(String string) {
            return Optional.ofNullable(fromString.get(string));
        }

        @Override
        public ActiveSlot getSlot() {
            return ActiveSlot.RIGHT_SHIFT;
        }
    }

    public enum Wildcard implements ActiveType {
        LIGHTNING_BOTTLE,
        ENTRENCH,
        FLAME_SPIRIT,
        PERMAFROST,
        PHANTOM_FORCE,
        RAPID_FIRE,
        WHIRLWIND,
        CONVERGENCE;

        private static final Map<String, Wildcard> fromString = new HashMap<>();

        static {
            fromString.put("Lightning Bottle", LIGHTNING_BOTTLE);
            fromString.put("Entrench", ENTRENCH);
            fromString.put("Flame Spirit", FLAME_SPIRIT);
            fromString.put("Permafrost", PERMAFROST);
            fromString.put("Phantom Force", PHANTOM_FORCE);
            fromString.put("Rapid Fire", RAPID_FIRE);
            fromString.put("Whirlwind", WHIRLWIND);
            fromString.put("Convergence", CONVERGENCE);
        }

        public static Optional<Wildcard> toEnum(String string) {
            return Optional.ofNullable(fromString.get(string));
        }

        @Override
        public ActiveSlot getSlot() {
            return ActiveSlot.WILDCARD;
        }
    }

    public enum Bow implements ActiveType {
        DIVINE_BEAM,
        EARTHQUAKE,
        PYROBLAST,
        PIERCING_COLD,
        DUMMY_DECOY,
        VOLLEY,
        SKYHOOK,
        DISCO_BALL;

        private static final Map<String, Bow> fromString = new HashMap<>();

        static {
            fromString.put("Divine Beam", DIVINE_BEAM);
            fromString.put("Earthquake", EARTHQUAKE);
            fromString.put("Pyroblast", PYROBLAST);
            fromString.put("Piercing Cold", PIERCING_COLD);
            fromString.put("Dummy Decoy", DUMMY_DECOY);
            fromString.put("Volley", VOLLEY);
            fromString.put("Skyhook", SKYHOOK);
            fromString.put("Disco Ball", DISCO_BALL);
        }

        public static Optional<Bow> toEnum(String string) {
            return Optional.ofNullable(fromString.get(string));
        }

        @Override
        public ActiveSlot getSlot() {
            return ActiveSlot.BOW;
        }
    }

    public enum Swap implements ActiveType {
        SPARK_OF_INSPIRATION,
        EARTHEN_WRATH,
        VOLCANIC_METEOR,
        AVALANCHE,
        CHAOS_DAGGER,
        GRAVITY_BOMB,
        THUNDERCLOUD_FORM,
        REFRACTION,
        COLOR_SPLASH;

        private static final Map<String, Swap> fromString = new HashMap<>();

        static {
            fromString.put("Spark of Inspiration", SPARK_OF_INSPIRATION);
            fromString.put("Earthen Wrath", EARTHEN_WRATH);
            fromString.put("Volcanic Meteor", VOLCANIC_METEOR);
            fromString.put("Avalanche", AVALANCHE);
            fromString.put("Chaos Dagger", CHAOS_DAGGER);
            fromString.put("Gravity Bomb", GRAVITY_BOMB);
            fromString.put("Thundercloud Form", THUNDERCLOUD_FORM);
            fromString.put("Refraction", REFRACTION);
            fromString.put("Color Splash", COLOR_SPLASH);
        }

        public static Optional<Swap> toEnum(String string) {
            return Optional.ofNullable(fromString.get(string));
        }

        @Override
        public ActiveSlot getSlot() {
            return ActiveSlot.SWAP;
        }
    }

    public enum Lifeline implements ActiveType {
        ETERNAL_SAVIOR,
        APOCALYPSE,
        CRYOBOX,
        ESCAPE_ARTIST,
        STEEL_STALLION,
        LAST_BREATH;

        private static final Map<String, Lifeline> fromString = new HashMap<>();

        static {
            fromString.put("Eternal Savior", ETERNAL_SAVIOR);
            fromString.put("Apocalypse", APOCALYPSE);
            fromString.put("Cryobox", CRYOBOX);
            fromString.put("Escape Artist", ESCAPE_ARTIST);
            fromString.put("Steel Stallion", STEEL_STALLION);
            fromString.put("Last Breath", LAST_BREATH);
        }

        public static Optional<Lifeline> toEnum(String string) {
            return Optional.ofNullable(fromString.get(string));
        }

        @Override
        public ActiveSlot getSlot() {
            return ActiveSlot.LIFELINE;
        }
    }
}
