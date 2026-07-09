package com.aquamancer.czlib.api.abils;

public class Passive {
    private final Passives ability;
    private final AbilitySpec spec;
    private final Rarity rarity;

    public Passive(Passives ability, AbilitySpec spec, Rarity rarity) {
        this.ability = ability;
        this.spec = spec;
        this.rarity = rarity;
    }

    public Passives getAbility() {
        return ability;
    }

    public AbilitySpec getSpec() {
        return spec;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public boolean equals(Object o2) {
        if (this == o2) return true;
        if (!(o2 instanceof Passive)) return false;
        return (this.ability == ((Passive) o2).ability);
    }

    @Override
    public int hashCode() {
        return this.ability.hashCode();
    }

    @Override
    public String toString() {
        return "Ability=" + ability + ", spec=" + spec + ", rarity=" + rarity;
    }
}
