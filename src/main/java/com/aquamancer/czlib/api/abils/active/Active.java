package com.aquamancer.czlib.api.abils.active;

import com.aquamancer.czlib.api.abils.AbilitySpec;
import com.aquamancer.czlib.api.abils.Rarity;

public class Active<T extends Enum<?>> {
    private T name;
    private AbilitySpec spec;
    private Rarity rarity;

    public Active(T name, AbilitySpec spec, Rarity rarity) {
        this.name = name;
        this.spec = spec;
        this.rarity = rarity;
    }
}
