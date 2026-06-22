package com.aquamancer.czlib.trinket;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TrinketOpener {
    // syncId increments by 1 on each gui change
    // opening chest increments syncid by 2?
    // DEPTHS GUI: every odd click = increment revision by 2
    // every even click = increment revision by 3
    // after opening depths gui, send revision: 1, 3, 6, 8, 11...
    // syncid stays the same


    public static int syncId = 1;
    public static boolean autoClick = false;

    public static int delayMillis = 100;
    public static int millisUntilSend = 0;
    public static ClickSlotC2SPacket payload = null;

    public static void onRMB(ClickSlotC2SPacket packet) {
        MinecraftClient.getInstance().execute(() -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.player.sendMessage(Text.literal("ActionType: " + packet.getActionType().name()));
            client.player.sendMessage(Text.literal("Button: " + packet.getButton()));
            client.player.sendMessage(Text.literal("ModifiedStacks: " + packet.getModifiedStacks().toString()));
            client.player.sendMessage(Text.literal("Revision: " + packet.getRevision() + "\n" + "ScreenHandlerRevision: " + client.player.currentScreenHandler.getRevision()));
            client.player.sendMessage(Text.literal("Slot: " + packet.getSlot()));
            client.player.sendMessage(Text.literal("Stack: " + packet.getStack()));
            client.player.sendMessage(Text.literal("SyncId: " + packet.getSyncId()));

            Item item = Registries.ITEM.get(new Identifier("minecraft", "jigsaw"));
            ItemStack stack = new ItemStack(item, 1);

            Int2ObjectMap<ItemStack> modifiedStacks = new Int2ObjectOpenHashMap<>();
            modifiedStacks.put(53, ItemStack.EMPTY);

            if (packet.getStack().getName().getString().equalsIgnoreCase("Depths Trinket")) {
                client.player.sendMessage(Text.literal("trinket clicked"));
                int revision = 1;
                millisUntilSend = delayMillis;
                payload = new ClickSlotC2SPacket(
                                syncId,
                                1,
                                53,
                                0,
                                SlotActionType.PICKUP,
                                stack,
                                modifiedStacks
                        );
                client.getNetworkHandler().getConnection().send(payload);
                payload = null;
            }
        });
    }

    public static void onTick(MinecraftClient client) {
        if (millisUntilSend > 0) {
            millisUntilSend -= 50;
        }
        if (payload != null && millisUntilSend <= 0) {
            client.getNetworkHandler().getConnection().send(payload);
            payload = null;
        }
    }
}
