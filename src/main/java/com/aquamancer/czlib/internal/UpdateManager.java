package com.aquamancer.czlib.internal;

import net.minecraft.client.MinecraftClient;
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

    private static final List<Integer> DEFAULT_HEAD_SLOTS = List.of(47, 48, 50, 51);
    static {
        WorldChangeTracker.register((world) -> getInstance().onWorldChange());
    }

    private Set<Integer> headSlotsToClick = new HashSet<>(DEFAULT_HEAD_SLOTS);
    private int lastScreenSyncId = 0;

    private UpdateManager() {}

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

    public void onWorldChange() {
        this.lastScreenSyncId = 0;
    }

    void setHeadsToClick(Collection<Integer> slots) {
        headSlotsToClick = new HashSet<>(slots);
        headSlotsToClick.remove(SelfIdentifier.getSelfHeadSlot());
    }

    // todo make package-private
    public void update() {
        int trinketSlot = TrinketLocator.getTrinketSlot();
        if (trinketSlot == -1) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.player == null) return;
            client.player.sendMessage(Text.literal("Could not find Depths Trinket in inventory"));
            return;
        }
        TrinketOpener.clickPartyHeads(lastScreenSyncId, headSlotsToClick, trinketSlot);
    }
}
