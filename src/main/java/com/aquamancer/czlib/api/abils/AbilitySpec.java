package com.aquamancer.czlib.api.abils;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AbilitySpec {
    DAWN("Dawnbringer", Spec.DAWN),
    EARTH("Earthbound", Spec.EARTH),
    FLAME("Flamecaller", Spec.FLAME),
    FROST("Frostborn", Spec.FROST),
    SHADOW("Shadowdancer", Spec.SHADOW),
    STEEL("Steelsage", Spec.STEEL),
    WIND("Windwalker", Spec.WIND),
    PRISMATIC("Prismatic", null);

    private final String displayName;
    private final Spec spec;

    AbilitySpec(String displayName, Spec spec) {
        this.displayName = displayName;
        this.spec = spec;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Optional<Spec> toSpec() {
        return Optional.ofNullable(spec);
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

    private static final Map<AbilitySpec, EnumSet<Actives>> actives = new EnumMap<>(AbilitySpec.class);
    static {
        for (Actives active : Actives.values()) {
            actives.computeIfAbsent(active.getSpec(), k -> EnumSet.noneOf(Actives.class))
                    .add(active);
        }
    }

    private static final Map<AbilitySpec, EnumSet<Passives>> passives = new EnumMap<>(AbilitySpec.class);
    static {
        for (Passives passive : Passives.values()) {
            passives.computeIfAbsent(passive.getSpec(), k -> EnumSet.noneOf(Passives.class))
                    .add(passive);
        }
    }

    public static Optional<AbilitySpec> toEnum(String string) {
        return Optional.ofNullable(FROM_STRING.get(string));
    }

    public static Optional<AbilitySpec> fromAbilityName(String ability) {
        return Actives.getSpec(ability).or(() -> Passives.getSpec(ability));
    }

    public static EnumSet<Actives> getActives(AbilitySpec spec) {
        return actives.get(spec);
    }

    public static EnumSet<Passives> getPassives(AbilitySpec spec) {
        return passives.get(spec);
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
