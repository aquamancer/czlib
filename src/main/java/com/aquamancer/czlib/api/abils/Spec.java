package com.aquamancer.czlib.api.abils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
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

    private static final Map<String, Spec> FROM_STRING = Arrays.stream(values())
            .collect(Collectors.toMap(Spec::getDisplayName, Function.identity()));

    private final String name;
    private final int color;

    Spec(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getDisplayName() {
        return this.name;
    }

    public int getColor() {
        return this.color;
    }

    public static Optional<Spec> fromString(String string) {
        return Optional.ofNullable(FROM_STRING.get(string));
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
    }
}
