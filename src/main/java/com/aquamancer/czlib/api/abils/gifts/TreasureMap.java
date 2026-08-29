package com.aquamancer.czlib.api.abils.gifts;

import com.aquamancer.czlib.api.rooms.Room;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class TreasureMap extends Gift {
    private static final Set<Room> ROOMS = EnumSet.of(
            Room.ABILITY,
            Room.ABILITY_ELITE,
            Room.UPGRADE,
            Room.UPGRADE_ELITE,
            Room.UTILITY,
            Room.BOSS
    );

    private final Set<Room> remaining = ROOMS;
    private boolean wildcard = false;

    public TreasureMap() {
        super(Gifts.TREASURE_MAP, ROOMS.size() + 1);
    }

    public boolean onRoomSpawned(Room room, boolean isWildcard) {
        if (!this.wildcard && isWildcard) {
            this.wildcard = true;
            return true;
        }
        return this.remaining.remove(room);
    }

    public Set<Room> getRemainingRooms() {
        return Collections.unmodifiableSet(this.remaining);
    }

    public boolean wildcardSelected() {
        return this.wildcard;
    }

    @Override
    public int getCounter() {
        return this.remaining.size() + ((this.wildcard) ? 0 : 1);
    }
}
