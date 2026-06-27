package com.aquamancer.czlib.api.abils;

public class Passive {
    private PassiveName name;
    private AbilitySpec spec;
    private Rarity rarity;

    public Passive(PassiveName name, AbilitySpec spec, Rarity rarity) {
        this.name = name;
        this.spec = spec;
        this.rarity = rarity;
    }
}
