package com.aquamancer.czlib.api.abils;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AbilitySpec {
    DAWN(
            "Dawnbringer",
            Spec.DAWN,
            EnumSet.of(
                    Actives.SOOTHING,
                    Actives.WARD_OF_LIGHT,
                    Actives.RADIANT_BLESSING,
                    Actives.BOTTLED_SUNLIGHT,
                    Actives.LIGHTNING_BOTTLE,
                    Actives.DIVINE_BEAM,
                    Actives.SPARK_OF_INSPIRATION,
                    Actives.ETERNAL_SAVIOR
            ),
            EnumSet.of(
                    Passives.ENLIGHTENMENT,
                    Passives.REJUVENATION,
                    Passives.SUNDROPS
            )
    ),
    EARTH(
            "Earthbound",
            Spec.EARTH,
            EnumSet.of(
                    Actives.EARTHEN,
                    Actives.BEASTS_CLAW,
                    Actives.TAUNT,
                    Actives.IRON_GRIP,
                    Actives.ENTRENCH,
                    Actives.EARTHQUAKE,
                    Actives.EARTHEN_WRATH
            ),
            EnumSet.of(
                    Passives.BRAMBLE_SHELL,
                    Passives.BULWARK,
                    Passives.TOUGHNESS
            )
    ),
    FLAME(
            "Flamecaller",
            Spec.FLAME,
            EnumSet.of(
                    Actives.VOLCANIC,
                    Actives.FIREBALL,
                    Actives.IGNEOUS_RUNE,
                    Actives.FLAMESTRIKE,
                    Actives.FLAME_SPIRIT,
                    Actives.PYROBLAST,
                    Actives.VOLCANIC_METEOR,
                    Actives.APOCALYPSE
            ),
            EnumSet.of(
                    Passives.DETONATION,
                    Passives.PRIMORDIAL_MASTERY,
                    Passives.PYROMANIA
            )
    ),
    FROST(
            "Frostborn",
            Spec.FROST,
            EnumSet.of(
                    Actives.FRIGID,
                    Actives.ICE_LANCE,
                    Actives.SNOWSTORM,
                    Actives.ICE_BARRIER,
                    Actives.PERMAFROST,
                    Actives.PIERCING_COLD,
                    Actives.AVALANCHE,
                    Actives.CRYOBOX
            ),
            EnumSet.of(
                    Passives.FROZEN_DOMAIN,
                    Passives.ICEBREAKER
            )
    ),
    SHADOW(
            "Shadowdancer",
            Spec.SHADOW,
            EnumSet.of(
                    Actives.DARK,
                    Actives.ADVANCING_SHADOWS,
                    Actives.CLOAK_OF_SHADOWS,
                    Actives.BLADE_FLURRY,
                    Actives.PHANTOM_FORCE,
                    Actives.DUMMY_DECOY,
                    Actives.CHAOS_DAGGER,
                    Actives.ESCAPE_ARTIST
            ),
            EnumSet.of(
                    Passives.BRUTALIZE,
                    Passives.DEADLY_STRIKE,
                    Passives.DETHRONER,
                    Passives.SHADOW_SLAM
            )
    ),
    STEEL(
            "Steelsage",
            Spec.STEEL,
            EnumSet.of(
                    Actives.FOCUSED,
                    Actives.SIDEARM,
                    Actives.SCRAPSHOT,
                    Actives.FIREWORK_BLAST,
                    Actives.RAPID_FIRE,
                    Actives.VOLLEY,
                    Actives.GRAVITY_BOMB,
                    Actives.STEEL_STALLION
            ),
            EnumSet.of(
                    Passives.SHARPSHOOTER,
                    Passives.SPLIT_ARROW
            )
    ),
    WIND(
            "Windwalker",
            Spec.WIND,
            EnumSet.of(
                    Actives.WINDSWEPT,
                    Actives.WIND_WALK,
                    Actives.GUARDING_BOLT,
                    Actives.AEROBLAST,
                    Actives.WHIRLWIND,
                    Actives.SKYHOOK,
                    Actives.THUNDERCLOUD_FORM,
                    Actives.LAST_BREATH
            ),
            EnumSet.of(
                    Passives.AEROMANCY,
                    Passives.DODGING,
                    Passives.ONE_WITH_THE_WIND,
                    Passives.RESTORING_DRAFT
            )
    ),
    PRISMATIC(
            "Prismatic",
            null,
            EnumSet.of(
                    Actives.SOLAR_RAY,
                    Actives.ENCORE,
                    Actives.CHROMA_BLADE,
                    Actives.CONVERGENCE,
                    Actives.DISCO_BALL,
                    Actives.REFRACTION,
                    Actives.COLOR_SPLASH
            ),
            EnumSet.of(
                    Passives.ABNORMALITY,
                    Passives.GENEROSITY,
                    Passives.CHARITY,
                    Passives.FLEXIBILITY,
                    Passives.MULTIPLICITY,
                    Passives.PROSPERITY,
                    Passives.REBIRTH,
                    Passives.DIVERSITY,
                    Passives.OPPORTUNITY
            )
    );

    private final String displayName;
    private final Spec spec;
    private final EnumSet<Actives> actives;
    private final EnumSet<Passives> passives;

    AbilitySpec(String displayName, Spec spec, EnumSet<Actives> actives, EnumSet<Passives> passives) {
        this.displayName = displayName;
        this.spec = spec;
        this.actives = actives;
        this.passives = passives;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Optional<Spec> toSpec() {
        return Optional.ofNullable(spec);
    }

    public EnumSet<Actives> getActives() {
        return EnumSet.copyOf(actives);
    }

    public EnumSet<Passives> getPassives() {
        return EnumSet.copyOf(passives);
    }

    public int getColor() {
        if (this.spec == null) {
            return 0xff9cf0;  // prismatic
        } else {
            return this.spec.getColor();
        }
    }

    private static final Map<String, AbilitySpec> FROM_STRING = Arrays.stream(values())
            .collect(Collectors.toMap(AbilitySpec::getDisplayName, Function.identity()));

    public static Optional<AbilitySpec> toEnum(String string) {
        return Optional.ofNullable(FROM_STRING.get(string));
    }

    public static Optional<AbilitySpec> fromAbilityName(String ability) {
        return Actives.getSpec(ability).or(() -> Passives.getSpec(ability));
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
            return Integer.compare(p1, p2);
        }
    }
}
