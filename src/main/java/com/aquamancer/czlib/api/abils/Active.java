package com.aquamancer.czlib.api.abils;

public class Active {
    private Actives name;
    private AbilitySpec spec;
    private Rarity rarity;

    public Active(Actives name, AbilitySpec spec, Rarity rarity) {
        this.name = name;
        this.spec = spec;
        this.rarity = rarity;
    }
}
