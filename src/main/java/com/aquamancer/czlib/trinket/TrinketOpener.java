package com.aquamancer.czlib.trinket;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

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

    private static Queue<ClickSlotC2SPacket> queue = new ArrayDeque<>();
    private static int delayMillis = 50;
    private static int counter = delayMillis;

    public static void openTrinket(int slot) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.player.currentScreenHandler == null) return;
        if (client.currentScreen instanceof HandledScreen) return;

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

    public static void clickPartyHeads(int syncId, List<Integer> slots, int fdelayMillis) {
        delayMillis = fdelayMillis;
        for (int i = 0; i < Math.min(slots.size(), revisionSequence.size()); i++) {
            queue.add(new ClickSlotC2SPacket(
                    syncId,
                    revisionSequence.get(i),
                    slots.get(i),
                    0,
                    SlotActionType.PICKUP,
                    playerHead,
                    modifiedStacks
            ));
//            sendPacket(new ClickSlotC2SPacket(
//                    syncId,
//                    revisionSequence.get(i),
//                    slots.get(i),
//                    0,
//                    SlotActionType.PICKUP,
//                    playerHead,
//                    modifiedStacks
//            ));
        }
    }

    public static void clickPartyHeads1(int syncId, Set<Integer> slots, int trinketSlot) {
        for (Integer slot : slots) {
            openTrinket(trinketSlot);
            sendPacket(new ClickSlotC2SPacket(
                    syncId++,
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
            MinecraftClient.getInstance().setScreen(null);
        }
    }


    public static void onTick(MinecraftClient client) {
        if ((counter -= 50) <= 0) {
            ClickSlotC2SPacket packet = queue.poll();
            if (packet != null) {
                sendPacket((ClickSlotC2SPacket) packet);
            }
            counter = delayMillis;
        }
    }

    private static void sendPacket(Packet<?> packet) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getNetworkHandler() == null || client.getNetworkHandler().getConnection() == null) return;
        client.getNetworkHandler().getConnection().send(packet);
    }
}
