package com.aquamancer.czlib.api.abils;

public class Active {
    private final ActiveType ability;
    private final AbilitySpec spec;
    private final Rarity rarity;

    public Active(ActiveType ability, AbilitySpec spec, Rarity rarity) {
        this.ability = ability;
        this.spec = spec;
        this.rarity = rarity;
    }

    public ActiveType getAbility() {
        return ability;
    }

    public AbilitySpec getSpec() {
        return spec;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public ActiveSlot getSlot() {
        return this.ability.getSlot();
    }

    @Override
    public boolean equals(Object o2) {
        if (this == o2) return true;
        if (!(o2 instanceof Active)) return false;
        return this.ability == ((Active) o2).ability;
    }

    @Override
    public int hashCode() {
        return this.ability.hashCode();
    }

    @Override
    public String toString() {
        return "Active=" + ability + ", Spec=" + spec + ", Rarity=" + rarity;
    }
}
