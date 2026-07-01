package com.aquamancer.czlib.api.abils;

public class Active {
    private ActiveType ability;
    private AbilitySpec spec;
    private Rarity rarity;

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
}
