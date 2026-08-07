package com.aquamancer.czlib.api.abils;

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

    private static final Map<String, Aspect> fromString =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Aspect::getDisplayName, Function.identity()));

    public static Optional<Aspect> fromString(String string) {
        return Optional.ofNullable(fromString.get(string));
    }
}
