package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import com.aquamancer.czlib.api.rooms.Rooms;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Optional;

public class ZenithApi {
    private static final ZenithApi instance = new ZenithApi();

    private final Party party = new Party();
    private int room = -1;
    private int floor = 1;
    private Rooms currentRoom = Rooms.TREE_SELECT;

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
        });
    }

    public static ZenithApi getInstance() {
        return instance;
    }

    public Map<String, PartyMember> getParty() {
        return Map.copyOf(this.party.getPlayers());
    }

    public Optional<PartyMember> getPlayer(String name) {
        return this.party.getPlayer(name);
    }

    public boolean isPartyMember(String name) {
        return this.party.getPlayer(name).isPresent();
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
    }
}
