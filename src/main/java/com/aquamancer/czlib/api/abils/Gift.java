package com.aquamancer.czlib.api.abils;

import com.aquamancer.czlib.api.rooms.Rooms;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public class Gift {
    private final Gifts gift;
    private int counter;
    private Set<Rooms> treasureMap;

    public Gift(Gifts gift) {
        this.gift = gift;
        if (gift == Gifts.TREASURE_MAP) {
            treasureMap = EnumSet.allOf(Rooms.class);
        }
    }

    public Gift(Gifts gift, int counter) {
        this(gift);
        this.counter = counter;
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
