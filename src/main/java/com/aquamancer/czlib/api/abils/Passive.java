package com.aquamancer.czlib.api.abils;

import com.aquamancer.czlib.internal.TooltipParser;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class Passive implements Ability<Passives>, HasAbilitySpec, HasRarity {
    private final Passives ability;
    private final AbilitySpec spec;
    private final Rarity rarity;

    public Passive(Passives ability, Rarity rarity) {
        this.ability = ability;
        this.spec = ability.getSpec();
        this.rarity = rarity;
    }

    public Passive(Passives ability, AbilitySpec spec, Rarity rarity) {
        this.ability = ability;
        this.spec = spec;
        this.rarity = rarity;
    }

    public Passives getAbility() {
        return ability;
    }

    @Override
    public String getDisplayName() {
        return this.ability.getDisplayName();
    }

    @Override
    public MutableText getText() {
        return this.getAbility().getText();
    }

    @Override
    public AbilitySpec getSpec() {
        return spec;
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    public boolean deepEquals(Passive o2) {
        if (o2 == null) return false;
        if (this == o2) return true;
        return this.ability == o2.ability
                && this.spec == o2.spec
                && this.rarity == o2.rarity;
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

    public static Optional<Passive> from(ItemStack item) {
        Optional<Passives> ability = Passives.from(item);
        if (ability.isEmpty()) return Optional.empty();
        List<Text> tooltip = item.getTooltip(null, TooltipContext.BASIC);
        if (tooltip.size() < 2) return Optional.empty();
        String line2 = tooltip.get(1).getString();
        TooltipParser.SpecRarityParseResult specAndRarity = TooltipParser.parseSpecRarity(line2);
        if (specAndRarity.spec().isEmpty() || specAndRarity.rarity().isEmpty()) return Optional.empty();

        return Optional.of(new Passive(ability.get(), specAndRarity.spec().get(), specAndRarity.rarity().get()));
    }
}
