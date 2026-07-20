package com.aquamancer.czlib.api.abils;

public class Active implements HasAbilitySpec, HasRarity {
    private final Actives ability;
    private final AbilitySpec spec;
    private final Rarity rarity;

    public Active(Actives ability, AbilitySpec spec, Rarity rarity) {
        this.ability = ability;
        this.spec = spec;
        this.rarity = rarity;
    }

    public Actives getAbility() {
        return ability;
    }

    @Override
    public AbilitySpec getSpec() {
        return spec;
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    public ActiveSlot getSlot() {
        return this.ability.getSlot();
    }

    public boolean deepEquals(Active o2) {
        if (o2 == null) return false;
        if (this == o2) return true;
        return this.ability == o2.ability
                && this.spec == o2.spec
                && this.rarity == o2.rarity;
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
