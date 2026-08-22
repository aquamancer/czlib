package com.aquamancer.czlib.api.abils.gifts;

import com.aquamancer.czlib.api.rooms.Rooms;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class TreasureMap extends Gift {
    private static final Set<Rooms> ROOMS = EnumSet.of(
            Rooms.ABILITY,
            Rooms.ABILITY_ELITE,
            Rooms.UPGRADE,
            Rooms.UPGRADE_ELITE,
            Rooms.UTILITY,
            Rooms.BOSS
    );

    private final Set<Rooms> remaining = ROOMS;
    private boolean wildcard = false;

    public TreasureMap() {
        super(Gifts.TREASURE_MAP, ROOMS.size() + 1);
    }

    public boolean onRoomSpawned(Rooms room, boolean isWildcard) {
        if (!this.wildcard && isWildcard) {
            this.wildcard = true;
            return true;
        }
        return this.remaining.remove(room);
    }

    public Set<Rooms> getRemainingRooms() {
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
