package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

@ApiStatus.Internal
public class Party {
    private final Map<String, PartyMember> players = new HashMap<>();

    Party() {}

    Map<String, PartyMember> getPlayers() {
        return this.players;
    }

    Optional<PartyMember> getPlayer(String name) {
        return Optional.ofNullable(players.get(name));
    }

    public void setMembers(Set<String> names) {
        players.keySet().retainAll(names);
        for (String name : names) {
            players.putIfAbsent(name, new PartyMember(name));
        }
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

    public void setPassives(String player, List<Passive> passives, EnumSet<Curse> curses) {
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

    public void setActives(String player, List<Active> actives) {
        EnumMap<ActiveSlot, Active> nonWildcards = new EnumMap<>(ActiveSlot.class);
        List<Active> wildcards = new ArrayList<>();
        for (Active active : actives) {
            ActiveSlot slot = active.getSlot();
            if (slot == ActiveSlot.WILDCARD) {
                wildcards.add(active);
            } else {
                nonWildcards.put(slot, active);
            }
        }

        players.get(player).setActives(nonWildcards);
        players.get(player).setWildcards(wildcards);
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