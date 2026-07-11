package com.aquamancer.czlib.api.abils;

import com.aquamancer.czlib.api.rooms.Rooms;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public class Gift {
    private static final EnumSet<Rooms> TREASURE_MAP_ROOMS = EnumSet.of(
            Rooms.ABILITY,
            Rooms.ABILITY_ELITE,
            Rooms.UPGRADE,
            Rooms.UPGRADE_ELITE,
            Rooms.UTILITY,
            Rooms.BOSS
    );

    private final Gifts gift;
    private int counter;
    private EnumSet<Rooms> treasureMap;

    public Gift(Gifts gift) {
        this.gift = gift;
        if (gift == Gifts.TREASURE_MAP) {
            treasureMap = EnumSet.copyOf(TREASURE_MAP_ROOMS);
        }
    }

    public Gift(Gifts gift, int counter) {
        this(gift);
        this.counter = counter;
    }

    public int getCounter() {
        return this.counter;
    }

    public int decrement() {
        return --this.counter;
    }

    public int increment() {
        return ++this.counter;
    }

    @Override
    public boolean equals(Object o2) {
        if (this == o2) return true;
        if (!(o2 instanceof Gift)) return false;
        return this.gift == ((Gift) o2).gift;
    }

    @Override
    public int hashCode() {
        return gift.hashCode();
    }
}
