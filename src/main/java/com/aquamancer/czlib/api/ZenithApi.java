package com.aquamancer.czlib.api;

public class ZenithApi {
    private static ZenithApi instance;

    private final Party party = new Party();
    private int room = -1;
    private int floor = 1;

    private ZenithApi() {}

    public static ZenithApi getInstance() {
        if (instance == null) {
            instance = new ZenithApi();
        }
        return instance;
    }

    public Party getParty() {
        return this.party;
    }
}
