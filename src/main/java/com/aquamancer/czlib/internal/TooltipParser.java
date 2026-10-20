package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.api.abils.AbilitySpec;
import com.aquamancer.czlib.api.abils.Rarity;

import java.util.Optional;

public class TooltipParser {
    private static final String SPEC_RARITY_SPLIT = " : ";

    public record SpecRarityParseResult(Optional<AbilitySpec> spec, Optional<Rarity> rarity) {}
    public static SpecRarityParseResult parseSpecRarity(String line) {
        line = line.trim();

        int split = line.indexOf(SPEC_RARITY_SPLIT);
        if (split <= 0) return new SpecRarityParseResult(Optional.empty(), Optional.empty());
        int rarityStart = split + SPEC_RARITY_SPLIT.length();
        if (rarityStart == line.length()) return new SpecRarityParseResult(Optional.empty(), Optional.empty());  // no chars after split
        // get the last word in case of enlightenment or an upgrade selection which have 2 rarities shown
        String rarityPart = line.substring(rarityStart - 1);  // include a leading space
        int lastSpace = rarityPart.lastIndexOf(' ');

        Optional<AbilitySpec> spec = AbilitySpec.fromString(line.substring(0, split));
        Optional<Rarity> rarity = Rarity.fromString(rarityPart.substring(lastSpace + 1));
        return new SpecRarityParseResult(spec, rarity);
    }
}
