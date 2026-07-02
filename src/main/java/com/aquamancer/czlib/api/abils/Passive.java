package com.aquamancer.czlib.api.abils;

public class Passive {
    private Passives name;
    private AbilitySpec spec;
    private Rarity rarity;

    public Passive(Passives name, AbilitySpec spec, Rarity rarity) {
        this.name = name;
        this.spec = spec;
        this.rarity = rarity;
    }

    @Override
    public String toString() {
        return "Ability=" + name + ", spec=" + spec + ", rarity=" + rarity;
    }
}
