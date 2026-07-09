package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.Gift;
import com.aquamancer.czlib.api.abils.Gifts;

import java.util.HashSet;
import java.util.Set;

public class GiftManager {
    private Set<Gift> gifts = new HashSet<>();

    public void addGift(Gift gift) {
        this.gifts.add(gift);
    }
}
