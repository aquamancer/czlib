package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;
import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import com.aquamancer.czlib.api.event.ZenithApiUpdateEvents;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

@ApiStatus.Internal
public class Party {
    private final Map<String, PartyMember> players = new HashMap<>();

    Party() {
        ZenithApiStateEvents.ROOM_SPAWNED.register((room, wildcard) -> {
            players.values().forEach(player -> player.onRoomSpawned(room, wildcard));
        });
        ZenithApiStateEvents.GRAVE_SPAWNED.register((deadPlayer) -> {
            players.computeIfPresent(deadPlayer, (name, player) -> {
                player.onDeath();
                return player;
            });
        });
    }

    Map<String, PartyMember> getPlayers() {
        return this.players;
    }

    Optional<PartyMember> getPlayer(String name) {
        return Optional.ofNullable(players.get(name));
    }

    public void setMembers(Set<String> names) {
        boolean changed = players.keySet().retainAll(names);
        for (String name : names) {
            if (players.putIfAbsent(name, new PartyMember(name)) == null) {
                changed = true;
            }
        }
        if (changed) ZenithApiUpdateEvents.PARTY_MEMBER.invoker().onPartyUpdate(players.keySet());
    }

    public void setGraveTimer(String player, double timer) {
        this.players.get(player).setGraveTimer(timer);
    }

    public void setGraveTimers(Map<String, Optional<Double>> timers) {
        for (Map.Entry<String, Optional<Double>> timer : timers.entrySet()) {
            if (timer.getValue().isEmpty()) continue;
            players.get(timer.getKey()).setGraveTimer(timer.getValue().get());
        }
    }

    public void setPassives(String player, EnumMap<Passives, Passive> passives, EnumSet<Curse> curses) {
        PartyMember p = players.get(player);
        p.setPassives(passives);
        p.setCurses(curses);
    }

    public void setSpecs(String player, EnumSet<Spec> specs) {
        players.get(player).setSpecs(specs);
    }

    public void setAspect(String player, Aspect aspect) {
        players.get(player).setAspect(aspect);
    }

    public void setActives(String player, Set<Active> actives) {
        players.get(player).setActives(actives);
    }

    public void createMember(String name) {
        if (players.putIfAbsent(name, new PartyMember(name)) == null) {
            ZenithApiUpdateEvents.PARTY_MEMBER.invoker().onPartyUpdate(players.keySet());
        }
    }

    public void addAbility(String player, Passive passive) {
        players.get(player).addAbility(passive);
    }

    public void addAbility(String player, Curse curse) {
        players.get(player).addAbility(curse);
    }

    public void addAbility(String player, Active active) {
        players.get(player).addAbility(active);
    }

    public void loseAbility(String player, Passives passive) {
        players.get(player).loseAbility(passive);
    }

    public void loseAbility(String player, Curse curse) {
        players.get(player).loseAbility(curse);
    }

    public void loseAbility(String player, Actives active) {
        players.get(player).loseAbility(active);
    }

    public void addSpec(String player, Spec spec) {
        players.computeIfPresent(player, (k, v) -> {
            v.addSpec(spec);
            return v;
        });
    }

    public void loseSpec(String player, Spec spec) {
        players.computeIfPresent(player, (k, v) -> {
            v.loseSpec(spec);
            return v;
        });
    }

    public void downgradeAll(String player) {
        players.computeIfPresent(player, (k, v) -> {
            v.downgradeAll();
            return v;
        });
    }

    public void upgradeBy2(String player) {
        players.computeIfPresent(player, (k, v) -> {
            v.upgradeBy2();
            return v;
        });
    }

    public void addGift(String player, Gifts gift) {
        players.get(player).addGift(gift);
    }

    public void addGift(String player, Gift gift) {
        players.get(player).addGift(gift);
    }

    public void setCharmLines(String player, EnumMap<Spec, Integer> charmLines) {
        players.get(player).setCharmLines(charmLines);
    }

    void clear() {
        if (!players.isEmpty()) {
            players.clear();
            ZenithApiUpdateEvents.PARTY_MEMBER.invoker().onPartyUpdate(players.keySet());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        this.players.forEach((name, player) -> {
            result.append(player.toString());
            result.append("\n\n");
        });
        return result.toString();
    }
}