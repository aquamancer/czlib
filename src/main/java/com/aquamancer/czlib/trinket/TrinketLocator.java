package com.aquamancer.czlib.trinket;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class TrinketLocator {
    // inventory packets have syncId = 0, 46 ItemStacks = 5 crafting + 4 armor + 36 inv (top-bot left-right) + 1 offhand
    private static final int EXPECTED_INV_SIZE = 46;
    private static final int INV_START = 9;
    private static final int INV_STOP = 45;  // look in offhand too ig
    private static final String DEPTHS_TRINKET = "Depths Trinket";

    private static InventoryS2CPacket lastInventoryPacket;
    private static boolean cached = false;
    private static int currentTrinketSlot = -1;
    private static int lastTrinketSlot = 13;  // "random" guess (most players have the trinket here)

    public static int getTrinketSlot() {
        if (!cached) {
            locateTrinket();
        }
        return currentTrinketSlot;
    }

    private static void locateTrinket() {
        cached = true;
        currentTrinketSlot = -1;
        List<ItemStack> inv = lastInventoryPacket.getContents();
        if (lastInventoryPacket == null || inv.size() < EXPECTED_INV_SIZE) return;
        // look for trinket in last known slot first
        if (lastTrinketSlot >= 0 && lastTrinketSlot < inv.size() && inv.get(lastTrinketSlot).getName().getString().equals(DEPTHS_TRINKET)) {
            currentTrinketSlot = lastTrinketSlot;
            return;
        }
        for (int i = INV_START; i <= INV_STOP; i++) {
            if (i == lastTrinketSlot) continue;  // already checked
            if (inv.get(i).getName().getString().equals(DEPTHS_TRINKET)) {
                currentTrinketSlot = i;
                return;
            }
        }
    }

    public static void onInventoryS2CPacket(InventoryS2CPacket packet) {
        lastInventoryPacket = packet;
        cached = false;
        lastTrinketSlot = currentTrinketSlot;
    }
}
