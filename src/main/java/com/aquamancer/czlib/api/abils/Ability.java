package com.aquamancer.czlib.api.abils;

import com.aquamancer.czlib.api.abils.gifts.Gifts;
import net.minecraft.text.MutableText;

import java.util.Optional;

public interface Ability<E extends Enum<E>> {
    E getAbility();
    String getDisplayName();
    MutableText getText();

    static Optional<Ability<?>> fromString(String displayName) {
        Optional<Actives> a = Actives.from(displayName);
        if (a.isPresent()) return Optional.of(a.get());
        Optional<Curse> c = Curse.from(displayName);
        if (c.isPresent()) return Optional.of(c.get());
        Optional<Passives> p = Passives.from(displayName);
        if (p.isPresent()) return Optional.of(p.get());
        Optional<Gifts> g = Gifts.from(displayName);
        if (g.isPresent()) return Optional.of(g.get());
        Optional<Aspect> aspect = Aspect.from(displayName);
        if (aspect.isPresent()) return Optional.of(aspect.get());

        return Optional.empty();
    }
}
