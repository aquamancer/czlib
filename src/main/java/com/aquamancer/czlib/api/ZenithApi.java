package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import com.aquamancer.czlib.api.rooms.Rooms;
import com.aquamancer.czlib.api.screens.ZenithScreens;
import com.aquamancer.czlib.internal.SelfIdentifier;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Optional;

public class ZenithApi {
    private static final ZenithApi instance = new ZenithApi();

    private final Party party = new Party();
    private int room = -1;
    private int floor = 1;
    private Rooms currentRoom = Rooms.TREE_SELECT;

    private boolean cleansed = false;
    private boolean mutated = false;
    private boolean diversityAchieved = false;

    private String openedTrinketPlayer = "";

    private ZenithApi() {
        ZenithApiStateEvents.ENTER_ZENITH_SHARD.register((p, c) -> this.reset());
        ZenithApiStateEvents.ROOM_SPAWNED.register((room, isWildcard) -> {
            if (room != Rooms.BOSS_CLEANSE) {
                this.room++;
            }

            if (this.currentRoom == Rooms.TREE_SELECT) {
                this.currentRoom = Rooms.ABILITY_SELECT;
            } else {
                this.currentRoom = room;
            }
        });
        ZenithApiStateEvents.SENT_TO_NEXT_FLOOR.register(() -> {
            this.floor++;
            this.room = 0;
            this.currentRoom = Rooms.PRE_FLOOR;
            this.cleansed = false;
            this.mutated = false;
        });
    }

    public static ZenithApi getInstance() {
        return instance;
    }

    public Map<String, PartyMember> getParty() {
        return this.party.getPlayers();
    }

    public Optional<PartyMember> getPlayer(String name) {
        return this.party.getPlayer(name);
    }

    public Optional<PartyMember> getSelf() {
        return getPlayer(getSelfName());
    }

    public String getSelfName() {
        return SelfIdentifier.getSelfName();
    }

    public boolean isSelf(String name) {
        return name.equals(getSelfName());
    }

    public boolean isSelf(PartyMember player) {
        PartyMember self = this.getSelf().orElse(null);
        return self != null && (player == self || isSelf(self.getName()));
    }

    public boolean isPartyMember(String name) {
        return this.party.getPlayer(name).isPresent();
    }

    public Rooms getCurrentRoomType() {
        return this.currentRoom;
    }
    
    public int getCurrentRoom() {
        return this.room;
    }
    
    public int getCurrentFloor() {
        return this.floor;
    }

    public void cleansed() {
        this.cleansed = true;
    }

    public boolean hasCleansed() {
        return this.cleansed;
    }

    public void mutated() {
        this.mutated = true;
    }

    public boolean hasMutated() {
        return this.mutated;
    }

    public void achieveDiversity() {
        this.diversityAchieved = true;
    }

    public boolean hasAchievedDiversity() {
        return this.diversityAchieved;
    }

    public Optional<PartyMember> getCurrentlySelectedInTrinket() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen == null) return Optional.empty();
        if (ZenithScreens.fromString(client.currentScreen.getTitle().getString()).orElse(null) != ZenithScreens.TRINKET) return Optional.empty();

        return getPlayer(this.openedTrinketPlayer);
    }

    @ApiStatus.Internal
    public void setCurrentlySelected(String name) {
        this.openedTrinketPlayer = name;
    }

    @ApiStatus.Internal
    public Party getPartyManager() {
        return this.party;
    }

    private void reset() {
        this.party.clear();
        this.room = -1;
        this.floor = 1;
        this.currentRoom = Rooms.TREE_SELECT;
        this.cleansed = false;
        this.mutated = false;
        this.diversityAchieved = false;
    }
}
