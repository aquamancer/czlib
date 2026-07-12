package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.internal.event.ZenithApiInternalEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
/*
update: on chat message, after closing ability selection, room generated, Ability removed! action bar
 */
@ApiStatus.Internal
public class UpdateManager {
    private static UpdateManager INSTANCE;
    static {
        ZenithApiInternalEvents.WORLD_CHANGED.register(() -> getInstance().onWorldChange());
        ClientTickEvents.START_CLIENT_TICK.register((client) -> getInstance().onTick());
    }

    private Map<String, Integer> headNames = new HashMap<>(4);
    private int lastScreenSyncId = 0;


    private static final int CHAT_UPDATE_DELAY_TICKS = 20;

    private int ticksUntilUpdate = CHAT_UPDATE_DELAY_TICKS;
    // update rules
    public void onManualScreenClose(Screen closedScreen) {
        if (closedScreen == null) return;
        String title = closedScreen.getTitle().getString();
        if (title.equals("Crafting") || title.equals("Current Abilities")) return;
        this.update(SelfIdentifier.getSelfName());
    }

    public void onActionBarMessage(Text message) {
        if (message.getString().equals("Ability removed!")) {
            this.update(SelfIdentifier.getSelfName());
        }
    }

    public void onZenithChatMessage() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;
        client.player.sendMessage(Text.literal("Zenith chat message"));
        this.ticksUntilUpdate = CHAT_UPDATE_DELAY_TICKS;
    }

    public void onTick() {
        if (ticksUntilUpdate == 0) {
            this.updateAll();
            ticksUntilUpdate--;  // go to -1 to indicate idling
        } else if (ticksUntilUpdate > 0) {
            ticksUntilUpdate--;
        }
    }

    // internals

    // todo make package-private
    public static UpdateManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UpdateManager();
        }
        return INSTANCE;
    }

    public void onOpenScreenPacket(OpenScreenS2CPacket packet) {
        this.lastScreenSyncId = packet.getSyncId();
    }

    private void onWorldChange() {
        this.lastScreenSyncId = 0;
    }

    void setHeadNames(Map<String, Integer> slotMapping) {
        this.headNames = slotMapping;
    }

    private void updateAll() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;
        if (client.currentScreen instanceof HandledScreen) return;


        int trinketSlot = TrinketLocator.getTrinketSlot();
        Set<Integer> slotsToClick = new HashSet<>(headNames.values());
        slotsToClick.remove(SelfIdentifier.getSelfHeadSlot());
        client.player.sendMessage(Text.literal("Updating all players: "+slotsToClick));
        TrinketOpener.openAndClickHeads(slotsToClick, trinketSlot, this.lastScreenSyncId);
    }
    // todo make private
    public void update(String player) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;
        if (client.currentScreen instanceof HandledScreen) return;

        Set<Integer> slotsToClick;
        if (SelfIdentifier.isSelf(player)) {
            slotsToClick = null;
        } else {
            Integer headSlot = headNames.get(player);
            if (headSlot == null) return;
            slotsToClick = Set.of(headSlot);
        }

        int trinketSlot = TrinketLocator.getTrinketSlot();
        if (trinketSlot == -1) {
            // todo remove this debugging
            client.player.sendMessage(Text.literal("Could not find Depths Trinket in inventory"));
            return;
        }
        client.player.sendMessage(Text.literal("Attempting trinket update for: " + player +", clicking slots: "+slotsToClick));

        TrinketOpener.openAndClickHeads(slotsToClick, trinketSlot, this.lastScreenSyncId);
    }

    private UpdateManager() {}
}
