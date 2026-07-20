package com.aquamancer.czlib.api.abils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    TWISTED;

    private static final Map<String, Rarity> fromString = new HashMap<>();
    static {
        fromString.put("Common", COMMON);
        fromString.put("Uncommon", UNCOMMON);
        fromString.put("Rare", RARE);
        fromString.put("Epic", EPIC);
        fromString.put("Legendary", LEGENDARY);
        fromString.put("XXXXXX", TWISTED);
    }
    public static Optional<Rarity> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
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

    public class RarityComparator implements Comparator<HasRarity> {
        @Override
        public int compare(HasRarity o1, HasRarity o2) {
            Rarity r1 = o1.getRarity();
            Rarity r2 = o2.getRarity();
            if (r1 == null && r2 == null) return 0;
            if (r1 == null) return -1;
            if (r2 == null) return 1;

            return o1.getRarity().ordinal() - o2.getRarity().ordinal();
        }
    }
}
