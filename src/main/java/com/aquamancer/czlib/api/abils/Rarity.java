package com.aquamancer.czlib.api.abils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Rarity {
    COMMON("Common", 1, 0x9f929c),
    UNCOMMON("Uncommon", 2, 0x70bc6d),
    RARE("Rare", 3, 0x705eca),
    EPIC("Epic", 4, 0xcd5eca),
    LEGENDARY("Legendary", 5, 0xe49b20),
    TWISTED("XXXXXX", 6, 0x703663);

    private final String displayName;
    private final int level;
    private final int color;

    Rarity(String displayName, int level, int color) {
        this.displayName = displayName;
        this.level = level;
        this.color = color;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getLevel() {
        return this.level;
    }

    public int getColor() {
        return color;
    }

    public MutableText getText() {
        return Text.literal(getDisplayName()).withColor(getColor());
    }

    private static final Map<Integer, Rarity> fromInt =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            Rarity::getLevel,
                            Function.identity()
                    ));

    private static final Map<String, Rarity> fromString =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            Rarity::getDisplayName,
                            Function.identity()
                    ));

    public static Optional<Rarity> fromInt(int level) {
        return Optional.ofNullable(fromInt.get(level));
    }

    public static Optional<Rarity> fromString(String string) {
        if (string == null) return Optional.empty();
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
