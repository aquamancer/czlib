package com.aquamancer.czlib.api.abils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Rarity {
    COMMON("Common", 1),
    UNCOMMON("Uncommon", 2),
    RARE("Rare", 3),
    EPIC("Epic", 4),
    LEGENDARY("Legendary", 5),
    TWISTED("XXXXXX", 6);

    private final String displayName;
    private final int level;

    Rarity(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    private static final Map<String, Rarity> fromString =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            Rarity::getDisplayName,
                            Function.identity()
                    ));

    public String getDisplayName() {
        return this.displayName;
    }

    public int getLevel() {
        return this.level;
    }

    public static Optional<Rarity> fromString(String name) {
        return Optional.ofNullable(fromString.get(name));
    }

    public static Rarity downgrade(Rarity rarity) {
        return switch (rarity) {
            case TWISTED -> TWISTED;
            case LEGENDARY -> EPIC;
            case EPIC -> RARE;
            case RARE -> UNCOMMON;
            case UNCOMMON, COMMON -> COMMON;
        };
    }

    public static Rarity megahammer(Rarity rarity) {
        return switch (rarity) {
            case TWISTED -> TWISTED;
            case LEGENDARY -> LEGENDARY;
            case EPIC, UNCOMMON, COMMON -> EPIC;
            case RARE -> RARE;
        };
    }

    public static Rarity upgradeBy2(Rarity rarity) {
        return switch (rarity) {
            case TWISTED -> TWISTED;
            case RARE, EPIC, LEGENDARY -> LEGENDARY;
            case UNCOMMON -> EPIC;
            case COMMON -> RARE;
        };
    }

    public static class RarityComparator implements Comparator<HasRarity> {
        @Override
        public int compare(HasRarity o1, HasRarity o2) {
            Rarity r1 = o1.getRarity();
            Rarity r2 = o2.getRarity();
            if (r1 == null && r2 == null) return 0;
            if (r1 == null) return -1;
            if (r2 == null) return 1;

            return Integer.compare(r1.level, r2.level);
        }
    }
}
