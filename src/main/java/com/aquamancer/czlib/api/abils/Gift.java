package com.aquamancer.czlib.api.abils;

import com.aquamancer.czlib.Czlib;
import com.aquamancer.czlib.api.rooms.Rooms;

import java.util.EnumSet;
import java.util.Optional;

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

    // dont want to make more classes
    private EnumSet<Rooms> treasureMap;
    private Spec callisPointedHat;

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

    public Gift(Spec callisPointedHat) {
        this.gift = Gifts.CALLICARPAS_POINTED_HAT;
        this.callisPointedHat = callisPointedHat;
        this.counter = Gifts.CALLICARPAS_POINTED_HAT.getDefaultValue();
    }

    public Gifts getGift() {
        return this.gift;
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

    public Optional<EnumSet<Rooms>> getTreasureMapRemaining() {
        if (this.gift != Gifts.TREASURE_MAP) {
            Czlib.LOGGER.error("Treasure map field queried on Gift of type Gifts: {}", this.gift);
            return Optional.empty();
        }
        return Optional.ofNullable(this.treasureMap);
    }

    public Optional<Spec> getCallisPointedHat() {
        if (this.gift != Gifts.CALLICARPAS_POINTED_HAT) {
            Czlib.LOGGER.error("Callicarpa's Hat spec field queried on Gift of type Gifts: {}", this.gift);
            return Optional.empty();
        }
        return Optional.ofNullable(this.callisPointedHat);
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
