package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;
import com.aquamancer.czlib.api.abils.gifts.Gift;
import com.aquamancer.czlib.api.abils.gifts.Gifts;
import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import com.aquamancer.czlib.api.event.ZenithApiUpdateEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

@ApiStatus.Internal
public class Party {
    private final Map<String, PartyMember> players = new HashMap<>();

    Party() {
        ZenithApiStateEvents.ROOM_SPAWNED.register((room, isWildcard) -> {
            players.values().forEach(player -> player.onRoomSpawned(room, isWildcard));
        });
        ZenithApiStateEvents.ROOM_REWARD.register((room, isWildcard) -> {
            players.values().forEach(player -> player.onRoomReward(room, isWildcard));
        });
        ZenithApiStateEvents.FLOOR_CLEARED.register((floor) -> {
            players.values().forEach(PartyMember::onFloorCleared);
        });
        ZenithApiStateEvents.SENT_TO_LOOTROOM.register(() -> {
            players.values().forEach(PartyMember::onSentToLootroom);
        });
        ZenithApiStateEvents.GRAVE_SPAWNED.register((deadPlayer) -> {
            players.computeIfPresent(deadPlayer, (name, player) -> {
                player.onDeath();
                return player;
            });
        });
        ZenithApiStateEvents.ENTER_NON_ZENITH_SHARD.register((p, c) -> {
            this.clear();
        });
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            boolean shouldUpdateEntities = false;
            for (PartyMember player : this.players.values()) {
                if (player.getEntity() == null || player.getEntity().isRemoved()) {
                    shouldUpdateEntities = true;
                }
            }
            if (shouldUpdateEntities) {
                client.world.getPlayers().forEach(entity -> {
                    this.players.computeIfPresent(entity.getName().getString(), (k, v) -> {
                        v.setEntity(entity);
                        return v;
                    });
                });
            }
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

    private PartyMember createMember(String name) {
        PartyMember newMember = new PartyMember(name);
        PartyMember after = players.computeIfAbsent(name, (n) -> {
            return newMember;
        });

        if (after == newMember) {
            ZenithApiUpdateEvents.PARTY_MEMBER.invoker().onPartyUpdate(players.keySet());
        }
        return after;
    }

    private void createMembers(Collection<String> names) {
        boolean changed = false;
        for (String name : names) {
            if (players.putIfAbsent(name, new PartyMember(name)) == null) {
                changed = true;
            }
        }
        if (changed) {
            ZenithApiUpdateEvents.PARTY_MEMBER.invoker().onPartyUpdate(players.keySet());
        }
    }

    public void setGraveTimer(String player, double timer) {
        createMember(player).setGraveTimer(timer);
    }

    public void setGraveTimers(Map<String, Optional<Double>> timers) {
        createMembers(timers.keySet());
        for (Map.Entry<String, Optional<Double>> timer : timers.entrySet()) {
            if (timer.getValue().isEmpty()) continue;
            players.get(timer.getKey()).setGraveTimer(timer.getValue().get());
        }
    }

    public void setPassives(String player, Set<Passive> passives, EnumSet<Curse> curses) {
        PartyMember p = createMember(player);
        p.setPassives(passives);
        p.setCurses(curses);
    }

    public void setSpecs(String player, EnumSet<Spec> specs) {
        createMember(player).setSpecs(specs);
    }

    public void setAspect(String player, Aspect aspect) {
        createMember(player).setAspect(aspect);
    }

    public void setActives(String player, Set<Active> actives) {
        createMember(player).setActives(actives);
    }

    public void addAbility(String player, Passive passive) {
        createMember(player).addAbility(passive);
    }

    public void addAbility(String player, Curse curse) {
        createMember(player).addAbility(curse);
    }

    public void addAbility(String player, Active active) {
        createMember(player).addAbility(active);
    }

    public void loseAbility(String player, Passives passive) {
        players.computeIfPresent(player, (n, p) -> {
            p.loseAbility(passive);
            return p;
        });
    }

    public void loseAbility(String player, Curse curse) {
        players.computeIfPresent(player, (n, p) -> {
            p.loseAbility(curse);
            return p;
        });
    }

    public void loseAbility(String player, Actives active) {
        players.computeIfPresent(player, (n, p) -> {
            p.loseAbility(active);
            return p;
        });
    }

    public void addSpec(String player, Spec spec) {
        createMember(player).addSpec(spec);
    }

    public void loseSpec(String player, Spec spec) {
        players.computeIfPresent(player, (n, p) -> {
            p.loseSpec(spec);
            return p;
        });
    }

    public void downgradeAll(String player) {
        players.computeIfPresent(player, (n, p) -> {
            p.downgradeAll();
            return p;
        });
    }

    public void upgradeBy2(String player) {
        players.computeIfPresent(player, (n, p) -> {
            p.upgradeBy2();
            return p;
        });
    }

    public void addGift(String player, Gifts gift) {
        createMember(player).addGift(gift);
    }

    public void addGift(String player, Gift gift) {
        createMember(player).addGift(gift);
    }

    public void setCharmLines(String player, EnumMap<Spec, Integer> charmLines) {
        createMember(player).setCharmLines(charmLines);
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