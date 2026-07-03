package com.aquamancer.czlib.trinket;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

@ApiStatus.Internal
public class UpdateManager {
    private static UpdateManager INSTANCE;

    private static final List<Integer> DEFAULT_HEAD_SLOTS = List.of(47, 48, 50, 51);
    static {
        WorldChangeTracker.register((world) -> getInstance().onWorldChange());
    }

    private Set<Integer> headSlotsToClick = new HashSet<>(DEFAULT_HEAD_SLOTS);
    private int selfHeadSlot = 0;
    private int trinketSlot = 13;  // 13 = top row middle of inv
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
    }

    // todo make package-private
    public void update() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        TrinketOpener.clickPartyHeads(lastScreenSyncId, headSlotsToClick, trinketSlot);
    }
}
