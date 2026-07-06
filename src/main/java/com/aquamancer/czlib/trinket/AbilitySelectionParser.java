package com.aquamancer.czlib.trinket;

import com.aquamancer.czlib.api.Party;
import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.api.abils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class AbilitySelectionParser {
    private static final Item EMPTY_SLOT = Registries.ITEM.get(new Identifier("minecraft", "light_gray_stained_glass_pane"));
    private static final int MIDDLE_ROW_START = 9;
    private static final int MIDDLE_ROW_END = 17;

    public static void onSlotClicked(int slot, ItemStack item, int button, SlotActionType slotActionType) {
        if (slot < MIDDLE_ROW_START || slot > MIDDLE_ROW_END) return;
        if (button != 0 || slotActionType != SlotActionType.PICKUP) return;
        if (item.getItem() == EMPTY_SLOT || item.isEmpty()) return;
        Screen openedScreen = MinecraftClient.getInstance().currentScreen;
        if (openedScreen == null) return;
        String title = openedScreen.getTitle().getString();
        switch (title) {
            case "Select an Aspect": {
                String ability = item.getName().getString();
                String self = SelfIdentifier.getSelfName();
                Party party = ZenithApi.getInstance().getPartyManager();
                party.createMember(self);

                Optional<Aspect> aspect = Aspect.toEnum(ability);
                if (aspect.isPresent()) party.setAspect(self, aspect.get());
                break;
            }
            case "Select an Ability":
            case "Select an Upgrade":
            case "Grimoire (Select Ability)":
            case "Regret (Replace Curse)": {
                String ability = item.getName().getString();
                String self = SelfIdentifier.getSelfName();
                Party party = ZenithApi.getInstance().getPartyManager();
                party.createMember(self);

                Optional<? extends ActiveType> active = Actives.toEnum(ability);
                Optional<Passives> passive = (active.isPresent()) ? Optional.empty() : Passives.toEnum(ability);
                if (active.isPresent() || passive.isPresent()) {
                    List<Text> tooltip = item.getTooltip(null, TooltipContext.BASIC);
                    if (tooltip.size() < 2) return;
                    String line2 = tooltip.get(1).getString();
                    TooltipParser.SpecRarityParseResult specAndRarity = TooltipParser.parseSpecRarity(line2);
                    Optional<AbilitySpec> spec = specAndRarity.spec();
                    Optional<Rarity> rarity = specAndRarity.rarity();
                    if (spec.isEmpty() || rarity.isEmpty()) return;

                    if (active.isPresent()) {
                        party.addAbility(self, new Active(active.get(), spec.get(), rarity.get()));
                    } else if (passive.isPresent()) {
                        party.addAbility(self, new Passive(passive.get(), spec.get(), rarity.get()));
                    }
                    return;
                }

                Optional<Curse> curse = Curse.toEnum(ability);
                if (curse.isPresent()) {
                    party.addAbility(self, curse.get());
                    return;
                }
                Optional<CelestialGift> gift = CelestialGift.toEnum(ability);
                if (gift.isPresent()) {
                    party.addGift(self, gift.get());
                    return;
                }
            }
            case "Webbing (Select Player)":
                // no reliable way to track webbing
                break;
            case "Pointed Hat (Select Tree)":

            case "Grimoire (Select Tree)":
            case "Poet's Quill (Remove Tree)":
            case "Poet's Quill (Replace Tree)":
            case "Prismatic Cube (Replace)":
            case "Regret (Remove Curse)":
        }
    }
}
