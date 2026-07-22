package com.aquamancer.czlib.api.abils;

import java.util.Optional;

public final class AbilityUtils {
    public static Optional<Enum<?>> fromString(String displayName) {
        Optional<Actives> a = Actives.fromString(displayName);
        if (a.isPresent()) return Optional.of(a.get());
        Optional<Curse> c = Curse.fromString(displayName);
        if (c.isPresent()) return Optional.of(c.get());
        Optional<Passives> p = Passives.fromString(displayName);
        if (p.isPresent()) return Optional.of(p.get());
        Optional<Gifts> g = Gifts.fromString(displayName);
        if (g.isPresent()) return Optional.of(g.get());
        Optional<Aspect> aspect = Aspect.fromString(displayName);
        if (aspect.isPresent()) return Optional.of(aspect.get());

        return Optional.empty();
    }

    private AbilityUtils() {}
}
