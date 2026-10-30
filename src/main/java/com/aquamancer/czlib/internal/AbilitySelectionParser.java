package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.api.Party;
import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.api.abils.*;
import com.aquamancer.czlib.api.abils.gifts.CallicarpasHat;
import com.aquamancer.czlib.api.abils.gifts.Gifts;
import com.aquamancer.czlib.api.screens.ZenithScreen;
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
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;

@ApiStatus.Internal
public class AbilitySelectionParser {
    private static final Item EMPTY_SLOT = Registries.ITEM.get(new Identifier("minecraft", "light_gray_stained_glass_pane"));
    private static final int MIDDLE_ROW_START = 9;
    private static final int MIDDLE_ROW_END = 17;

    public static void onSlotClicked(int slot, ScreenHandler screenHandler, int button, SlotActionType slotActionType) {
        if (slot < MIDDLE_ROW_START || slot > MIDDLE_ROW_END) return;
        if (slot >= screenHandler.getStacks().size()) return;
        if (button != 0 || slotActionType != SlotActionType.PICKUP) return;
        ItemStack item = screenHandler.getSlot(slot).getStack();
        if (item.getItem() == EMPTY_SLOT || item.isEmpty()) return;
        Screen openedScreen = MinecraftClient.getInstance().currentScreen;
        if (openedScreen == null) return;
        String title = openedScreen.getTitle().getString();
        ZenithScreen screen = ZenithScreen.fromString(title).orElse(null);
        if (screen == null) return;

        String self = SelfIdentifier.getSelfName();
        Party party = ZenithApi.getInstance().getPartyManager();
        party.createMember(self);
        switch (screen) {
            case ABILITY, UPGRADE, GENEROSITY, GRIMOIRE_ABILITY, STATUE_OF_REGRET_ADD -> {
                Optional<Active> active = Active.fromItem(item);
                if (active.isPresent()) {
                    party.addAbility(self, active.get());
                    return;
                }
                Optional<Passive> passive = Passive.fromItem(item);
                if (passive.isPresent()) {
                    party.addAbility(self, passive.get());
                    return;
                }
                Optional<Curse> curse = Curse.fromItem(item);
                if (curse.isPresent()) {
                    party.addAbility(self, curse.get());
                    return;
                }
                Optional<Gifts> gift = Gifts.fromItem(item);
                if (gift.isPresent()) {
                    party.addGift(self, gift.get());
                    return;
                }
            }
            case ASPECT -> {
                Optional<Aspect> aspect = Aspect.fromItem(item);
                aspect.ifPresent(value -> party.setAspect(self, value));
            }
            case QUILL_REMOVE -> {
                Optional<Spec> removed = Spec.fromString(item.getName().getString());
                removed.ifPresent(spec -> party.loseSpec(self, spec));
            }
            case QUILL_REPLACE -> {
                Optional<Spec> gained = Spec.fromString(item.getName().getString());
                gained.ifPresent(spec -> party.addSpec(self, spec));
            }
            case POINTED_HAT -> {
                Optional<Spec> tree = Spec.fromString(item.getName().getString());
                tree.ifPresent(spec -> party.addGift(self, new CallicarpasHat(spec.toAbilitySpec())));
            }
        }
    }
}
