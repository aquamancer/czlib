package com.aquamancer.czlib.internal;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

@ApiStatus.Internal
public class TrinketOpener {
    // syncId increments by 1 on each gui change
    // opening chest increments syncid by 2?
    // DEPTHS GUI: every odd click = increment revision by 2
    // every even click = increment revision by 3
    // after opening depths gui, send revision: 1, 3, 6, 8, 11...
    // syncid stays the same

    private static final List<Integer> revisionSequence = List.of(1, 3, 6, 8);

    private static final ItemStack playerHead = new ItemStack(Registries.ITEM.get(new Identifier("minecraft", "player_head")), 1);
    private static final ItemStack depthsTrinket = new ItemStack(Registries.ITEM.get(new Identifier("minecraft", "pink_dye")), 1);
    private static final Int2ObjectMap<ItemStack> modifiedStacks = new Int2ObjectOpenHashMap<>();
    static {
        modifiedStacks.put(53, ItemStack.EMPTY);
    }

    public static void openAndClickHeads(@Nullable Set<Integer> slots, int trinketSlot, int syncId) {
        if (trinketSlot < 9) return;  // inventory starts at slot 9
        if (slots == null || slots.isEmpty()) {
            openTrinket(trinketSlot);
            sendPacket(new CloseHandledScreenC2SPacket(syncId + 1));
        } else {
            for (Integer slot : slots) {
                openTrinket(trinketSlot);
                sendPacket(new ClickSlotC2SPacket(
                        ++syncId,
                        1,
                        slot,
                        0,
                        SlotActionType.PICKUP,
                        playerHead,
                        modifiedStacks
                ));
                sendPacket(new CloseHandledScreenC2SPacket(
                        syncId
                ));
            }
        }
    }

    private static void openTrinket(int slot) {
        if (slot < 9) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.player.currentScreenHandler == null) return;
        if (client.currentScreen instanceof HandledScreen) return;

        ScreenCanceler.cancelFutureScreens(1, ScreenCanceler.Type.TRINKET);

        Int2ObjectMap<ItemStack> modifiedStacks = new Int2ObjectOpenHashMap<>();
        modifiedStacks.put(slot, ItemStack.EMPTY);
        sendPacket(new ClickSlotC2SPacket(
                0,
                client.player.currentScreenHandler.getRevision(),
                slot,
                1,
                SlotActionType.PICKUP,
                depthsTrinket,
                modifiedStacks
        ));
    }


    private static void sendPacket(Packet<?> packet) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getNetworkHandler() == null || client.getNetworkHandler().getConnection() == null) return;
        client.getNetworkHandler().getConnection().send(packet);
    }
}
