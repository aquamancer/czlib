package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.api.Party;
import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.api.abils.*;
import com.aquamancer.czlib.api.screens.ZenithScreens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class AbilitySelectionParser {
    private static final Item EMPTY_SLOT = Registries.ITEM.get(new Identifier("minecraft", "light_gray_stained_glass_pane"));
    private static final int MIDDLE_ROW_START = 9;
    private static final int MIDDLE_ROW_END = 17;

    public static void onSlotClicked(int slot, ScreenHandler screenHandler, int button, SlotActionType slotActionType) {
        if (slot < MIDDLE_ROW_START || slot > MIDDLE_ROW_END) return;
        if (slot < 0 || slot >= screenHandler.getStacks().size()) return;
        if (button != 0 || slotActionType != SlotActionType.PICKUP) return;
        ItemStack item = screenHandler.getSlot(slot).getStack();
        if (item.getItem() == EMPTY_SLOT || item.isEmpty()) return;
        Screen openedScreen = MinecraftClient.getInstance().currentScreen;
        if (openedScreen == null) return;
        String title = openedScreen.getTitle().getString();
        ZenithScreens screen = ZenithScreens.fromString(title).orElse(null);
        if (screen == null) return;

        String self = SelfIdentifier.getSelfName();
        Party party = ZenithApi.getInstance().getPartyManager();
        switch (screen) {
            case ABILITY, UPGRADE, GENEROSITY, GRIMOIRE_ABILITY, STATUE_OF_REGRET_ADD: {
                String ability = item.getName().getString();
                party.createMember(self);

                Optional<Actives> active = Actives.fromString(ability);
                Optional<Passives> passive = (active.isPresent()) ? Optional.empty() : Passives.fromString(ability);
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

                Optional<Curse> curse = Curse.fromString(ability);
                if (curse.isPresent()) {
                    party.addAbility(self, curse.get());
                    return;
                }
                Optional<Gifts> gift = Gifts.fromString(ability);
                if (gift.isPresent()) {
                    party.addGift(self, gift.get());
                    return;
                }
                break;
            }
            case ASPECT:
                String ability = item.getName().getString();
                party.createMember(self);

                Optional<Aspect> aspect = Aspect.fromString(ability);
                if (aspect.isPresent()) party.setAspect(self, aspect.get());
                break;
            case QUILL_REMOVE: {
                Optional<Spec> removed = Spec.fromString(item.getName().getString());
                if (removed.isEmpty()) return;
                party.loseSpec(self, removed.get());
                break;
            }
            case QUILL_REPLACE: {
                Optional<Spec> gained = Spec.fromString(item.getName().getString());
                if (gained.isEmpty()) return;
                party.addSpec(self, gained.get());
                break;
            }
            case POINTED_HAT:
                Optional<Spec> tree = Spec.fromString(item.getName().getString());
                if (tree.isEmpty()) return;
                party.addGift(self, new Gift(tree.get()));
                break;
        }
    }
}
