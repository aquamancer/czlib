package com.aquamancer.czlib.api.abils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Spec {
    DAWN("Dawnbringer", 0xf0b326),
    EARTH("Earthbound", 0x6b3d2d),
    FLAME("Flamecaller", 0xf04e21),
    FROST("Frostborn", 0xa3cbe1),
    SHADOW("Shadowdancer", 0x7948af),
    STEEL("Steelsage", 0x929292),
    WIND("Windwalker", 0xc0dea9);

    private final String name;
    private final int color;

    Spec(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getDisplayName() {
        return this.name;
    }

    public AbilitySpec toAbilitySpec() {
        return AbilitySpec.fromSpec(this);
    }

    public int getColor() {
        return this.color;
    }

    private static final Map<String, Spec> fromString = Arrays.stream(values())
            .collect(Collectors.toMap(Spec::getDisplayName, Function.identity()));

    public static Optional<Spec> fromString(String string) {
        if (string == null) return Optional.empty();
        return Optional.ofNullable(fromString.get(string));
    }

    public static class SpecComparator implements Comparator<Spec> {
        private final Map<Spec, Integer> priority;
        public SpecComparator(Map<Spec, Integer> priority) {
            this.priority = priority;
        }

        @Override
        public int compare(Spec o1, Spec o2) {
            Integer p1 = priority.get(o1);
            Integer p2 = priority.get(o2);
            if (p1 == null && p2 == null) return 0;
            if (p1 == null) return -1;
            if (p2 == null) return 1;
            return Integer.compare(p1, p2);
        }

        public static SpecComparator fromAbilitySpec(Map<AbilitySpec, Integer> priority) {
            Map<Spec, Integer> convertedMap = new EnumMap<>(Spec.class);
            priority.forEach((abilitySpec, value) -> {
                Optional<Spec> converted = abilitySpec.toSpec();
                if (converted.isPresent()) {
                    convertedMap.put(converted.get(), value);
                }
            });
            return new SpecComparator(convertedMap);
        }
    }
}
