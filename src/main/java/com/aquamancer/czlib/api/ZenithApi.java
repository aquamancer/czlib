package com.aquamancer.czlib.api;

import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Optional;

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

    public Map<String, PartyMember> getParty() {
        return Map.copyOf(this.party.getPlayers());
    }

    public Optional<PartyMember> getPlayer(String name) {
        return this.party.getPlayer(name);
    }

    @ApiStatus.Internal
    public Party getPartyManager() {
        return this.party;
    }
}
