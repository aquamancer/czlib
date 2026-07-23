package com.aquamancer.czlib.api.abils;

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

    private static final Map<String, AbilitySpec> FROM_STRING = Arrays.stream(values())
            .collect(Collectors.toMap(AbilitySpec::getDisplayName, Function.identity()));

    private final String displayName;
    private final Spec spec;

    AbilitySpec(String displayName, Spec spec) {
        this.displayName = displayName;
        this.spec = spec;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Optional<Spec> getSpec() {
        return Optional.ofNullable(spec);
    }

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
            return p1 - p2;
        }
    }
}
