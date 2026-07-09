package com.aquamancer.czlib.api.abils;

public class Gift {
    private final Gifts gift;
    private int counter;

    public Gift(Gifts gift) {
        this.gift = gift;
    }

    public Gift(Gifts gift, int counter) {
        this.gift = gift;
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
