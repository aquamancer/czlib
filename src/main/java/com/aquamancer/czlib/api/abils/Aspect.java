package com.aquamancer.czlib.api.abils;

import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Aspect implements Ability<Aspect> {
    BOX("Mystery Box"),
    AXE("Aspect of the Axe"),
    BOW("Aspect of the Bow"),
    SCYTHE("Aspect of the Scythe"),
    SWORD("Aspect of the Sword"),
    WAND("Aspect of the Wand");

    private final String displayName;

    Aspect(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Aspect getAbility() {
        return this;
    }

    @Override
    public MutableText getText() {
        return Text.literal(this.getDisplayName());
    }

    private static final Map<String, Aspect> fromString =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Aspect::getDisplayName, Function.identity()));

    public static Optional<Aspect> from(String string) {
        if (string == null) return Optional.empty();
        return Optional.ofNullable(fromString.get(string));
    }

    public static Optional<Aspect> from(ItemStack item) {
        if (item == null) return Optional.empty();
        return from(item.getName().getString());
    }
}
