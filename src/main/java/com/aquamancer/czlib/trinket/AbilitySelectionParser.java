package com.aquamancer.czlib.trinket;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

public class AbilitySelectionParser {
    private static final Item EMPTY_SLOT = Registries.ITEM.get(new Identifier("minecraft", "light_gray_stained_glass_pane"));

    public static void onAbilityClicked(ItemStack item, int button, SlotActionType slotActionType) {
        if (button != 0 || slotActionType != SlotActionType.PICKUP) return;
        Screen openedScreen = MinecraftClient.getInstance().currentScreen;
        if (openedScreen == null) return;
        String title = openedScreen.getTitle().getString();
        if (!title.equals("Select an Ability") && !title.equals("Select an Upgrade")) return;
        if (item.getItem() == EMPTY_SLOT) return;

    }
}
