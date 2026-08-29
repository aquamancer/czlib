package com.aquamancer.czlib.api.abils.gifts;

import com.aquamancer.czlib.api.abils.Ability;
import com.aquamancer.czlib.api.rooms.Room;
import net.minecraft.text.MutableText;

import java.util.EnumSet;

public class Gift implements Ability<Gifts> {
    private final Gifts ability;
    private int counter;

    // dont want to make more classes
    private EnumSet<Room> treasureMap;

    public Gift(Gifts gift) {
        this.ability = gift;
        this.counter = gift.getDefaultValue();
    }

    public Gift(Gifts gift, int counter) {
        this(gift);
        this.counter = counter;
    }

    public Gifts getAbility() {
        return this.ability;
    }

    @Override
    public String getDisplayName() {
        return this.ability.getDisplayName();
    }

    @Override
    public MutableText getText() {
        return this.getAbility().getText();
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
        return this.ability == ((Gift) o2).ability;
    }

    @Override
    public int hashCode() {
        return ability.hashCode();
    }
}
