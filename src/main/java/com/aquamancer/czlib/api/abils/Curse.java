package com.aquamancer.czlib.api.abils;

import com.aquamancer.czlib.api.util.TextUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Curse implements Ability<Curse> {
    ANCHORING("Curse of Anchoring"),
    ARACHNOPHOBIA("Curse of Arachnophobia"),
    CHAOS("Curse of Chaos"),
    DEATH("Curse of Death"),
    DEPENDENCY("Curse of Dependency"),
    ENVY("Curse of Envy"),
    GLUTTONY("Curse of Gluttony"),
    GREED("Curse of Greed"),
    IMPATIENCE("Curse of Impatience"),
    LUST("Curse of Lust"),
    OBSCURITY("Curse of Obscurity"),
    PESSIMISM("Curse of Pessimism"),
    PRIDE("Curse of Pride"),
    REDUNDANCY("Curse of Redundancy"),
    RUIN("Curse of Ruin"),
    SLOTH("Curse of Sloth"),
    SOBRIETY("Curse of Sobriety");

    public static final int COLOR = 0xc41300;
    public static final int[] GRADIENT = {0xc41300, 0xa9112b, 0x8e013e};

    private final String displayName;

    Curse(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Curse getAbility() {
        return this;
    }

    @Override
    public MutableText getText() {
        return TextUtils.applyGradient(getDisplayName(), GRADIENT);
    }

    private static final Map<String, Curse> fromString =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Curse::getDisplayName, Function.identity()));

    public static Optional<Curse> from(String string) {
        if (string == null) return Optional.empty();
        return Optional.ofNullable(fromString.get(string));
    }

    public static Optional<Curse> from(ItemStack item) {
        if (item == null) return Optional.empty();
        return from(item.getName().getString());
    }
}
