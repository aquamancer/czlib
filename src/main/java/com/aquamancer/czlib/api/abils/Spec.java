package com.aquamancer.czlib.api.abils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Spec {
    DAWN("Dawnbringer"),
    EARTH("Earthbound"),
    FLAME("Flamecaller"),
    FROST("Frostborn"),
    SHADOW("Shadowdancer"),
    STEEL("Steelsage"),
    WIND("Windwalker");

    private static final Map<String, Spec> FROM_STRING = Arrays.stream(values())
            .collect(Collectors.toMap(Spec::getDisplayName, Function.identity()));

    private final String name;

    Spec(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return name;
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
            return p1 - p2;
        }
    }
}
