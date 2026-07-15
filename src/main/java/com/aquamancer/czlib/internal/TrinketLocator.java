package com.aquamancer.czlib.internal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiStatus.Internal
public class TrinketLocator {
    private static final int INVENTORY_SIZE = 36;
    private static final int INV_PACKET_SIZE = 46;
    private static final String DEPTHS_TRINKET = "Depths Trinket";

    private static final Map<ScreenHandlerType<? extends ScreenHandler>, Integer> slotOffsets = new HashMap<>();
    static {
        slotOffsets.put(null, 9);  // inventory
        slotOffsets.put(ScreenHandlerType.GENERIC_9X3, 27);
        slotOffsets.put(ScreenHandlerType.GENERIC_9X6, 54);
    }

    private static @Nullable Integer offset = null;
    private static InventoryS2CPacket lastInventoryPacket;
    private static boolean cached = false;
    private static int currentTrinketSlot = 4;
    private static int lastTrinketSlot = 4;  // "random" guess (most players have the trinket here)

    public static int getTrinketSlot() {
        if (!cached) {
            locateTrinket();
        }
        return currentTrinketSlot + slotOffsets.get(null);  // make relative to inventory
    }

    private static void locateTrinket() {
        if (offset == null) return;
        cached = true;
        currentTrinketSlot = -1;
        List<ItemStack> inv = lastInventoryPacket.getContents();
        int start = offset;
        int stop = offset + INVENTORY_SIZE - 1;
        if (inv.size() <= stop) return;
        // look for trinket in last known slot first
        int lastTrinketSlotRelative = lastTrinketSlot + offset;
        if (lastTrinketSlotRelative >= start && lastTrinketSlotRelative <= stop && inv.get(lastTrinketSlotRelative).getName().getString().equals(DEPTHS_TRINKET)) {
            currentTrinketSlot = lastTrinketSlot;
            return;
        }
        for (int i = start; i <= stop; i++) {
            if (inv.get(i).isEmpty()) {
                continue;
            }
            if (inv.get(i).getName().getString().equals(DEPTHS_TRINKET)) {
                currentTrinketSlot = i - offset;
                return;
            }
        }
    }

    public static void onInventoryS2CPacket(InventoryS2CPacket packet) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        if (packet.getSyncId() == 0 && packet.getContents().size() == INV_PACKET_SIZE) {
            lastInventoryPacket = packet;
            offset = slotOffsets.get(null);
            cached = false;
            lastTrinketSlot = currentTrinketSlot;
        } else {
            try {
                slotOffsets.computeIfPresent(client.player.currentScreenHandler.getType(), (k, slot) -> {
                    lastInventoryPacket = packet;
                    offset = slot;
                    cached = false;
                    lastTrinketSlot = currentTrinketSlot;
                    return slot;
                });
            } catch (UnsupportedOperationException ignored) {
                // ScreenHandler.class
//                public ScreenHandlerType<?> getType() {
//                    if (this.type == null) {
//                        throw new UnsupportedOperationException("Unable to construct this menu by type");
//                    } else {
//                        return this.type;
//                    }
//                }
            }
        }
    }
}
