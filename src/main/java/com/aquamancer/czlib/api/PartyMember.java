package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;
import com.aquamancer.czlib.api.abils.Gifts;
import com.aquamancer.czlib.api.event.ZenithApiEvents;
import com.aquamancer.czlib.api.rooms.Rooms;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PartyMember {
    private final String name;
    private double graveTimer;
    private EnumSet<Spec> specs = EnumSet.noneOf(Spec.class);
    private Set<Passive> passives = new HashSet<>();
    private EnumSet<Curse> curses = EnumSet.noneOf(Curse.class);
    private Aspect aspect;
    private EnumMap<ActiveSlot, Active> actives = new EnumMap<>(ActiveSlot.class);
    private Set<Active> wildcards = new HashSet<>();
    private EnumMap<Gifts, Gift> gifts = new EnumMap<>(Gifts.class);

    public PartyMember(String name) {
        this.name = name;

        ZenithApiEvents.ROOM_SPAWNED.register((room, wildcard) -> {
            gifts.computeIfPresent(Gifts.NORTHERN_STAR, (k, v) -> {
                if (room == Rooms.ABILITY_ELITE || room == Rooms.UPGRADE_ELITE) {
                    if (v.decrement() <= 0) {
                        return null;
                    }
                }
                return v;
            });

            gifts.computeIfPresent(Gifts.WILD_CARD, (k, v) -> {
                if (wildcard) {
                    v.increment();
                }
                return v;
            });


        });
    }

    void setGraveTimer(double time) {
        this.graveTimer = time;
    }

    void setPassives(Set<Passive> passives) {
        this.passives = passives;
    }

    void setCurses(EnumSet<Curse> curses) {
        this.curses = curses;
    }

    void setSpecs(EnumSet<Spec> specs) {
        this.specs = specs;
    }

    void setAspect(Aspect aspect) {
        this.aspect = aspect;
    }

    void setActives(EnumMap<ActiveSlot, Active> actives) {
        this.actives = actives;
    }

    void setWildcards(Set<Active> wildcards) {
        this.wildcards = wildcards;
    }

    void addAbility(Passive passive) {
        this.passives.add(passive);
    }

    void addAbility(Curse curse) {
        this.curses.add(curse);
    }

    void addAbility(Active active) {
        if (active.getSlot() == ActiveSlot.WILDCARD) {
            this.wildcards.add(active);
        } else {
            this.actives.put(active.getSlot(), active);
        }
    }

    void loseAbility(Passives passive) {
        this.passives.remove(new Passive(passive, null, null));
    }

    void loseAbility(Curse curse) {
        this.curses.remove(curse);
    }

    void loseAbility(ActiveType active) {
        actives.values().removeIf(e -> e.getAbility() == active);
        // todo verify removing convergence removes all wildcards
        if (active == Actives.Wildcard.CONVERGENCE) {
            wildcards.clear();
        } else {
            wildcards.removeIf(e -> e.getAbility() == active);
        }
    }

    void addSpec(Spec spec) {
        this.specs.add(spec);
    }

    private void replaceAll(Function<Active, Active> newActive, Function<Passive, Passive> newPassive) {
        this.actives.replaceAll((k, v) -> newActive.apply(v));
        this.wildcards = this.wildcards.stream().map(newActive).collect(Collectors.toSet());
        this.passives = this.passives.stream().map(newPassive).collect(Collectors.toSet());
    }

    private void replaceAll(Function<Rarity, Rarity> newRarity) {
        this.replaceAll(
                (old) -> new Active(old.getAbility(), old.getSpec(), newRarity.apply(old.getRarity())),
                (old) -> new Passive(old.getAbility(), old.getSpec(), newRarity.apply(old.getRarity()))
        );
    }

    void downgradeAll() {
        this.replaceAll(Rarity::downgrade);
    }

    void megaHammer() {
        this.replaceAll(Rarity::megahammer);
    }

    void upgradeBy2() {
        this.replaceAll(Rarity::upgradeBy2);
    }

    void invertSpecs() {
        this.specs = EnumSet.complementOf(this.specs);
    }

    void addGift(Gifts gift) {
        switch (gift) {
            // gifts that are passives
            case NORTHERN_STAR:
            case BOTTOMLESS_BOWL:
            case WILD_CARD:
            case AVARICIOUS_PENDANT:
            case COMB_OF_SELECTION:
            case PILLAR_OF_LIGHT:
            case BROKEN_CLOCK:
            case TREASURE_MAP:
            case CALLICARPAS_POINTED_HAT:
            case RAINBOW_GEODE:
            case CRACKED_IDOL:
                this.gifts.put(gift, new Gift(gift, Gifts.getDefaultValue(gift)));
                break;
            // gifts not fully handled by chat or gui
            case KALEIDOSCOPIC_LENS:
                this.invertSpecs();
                break;
            case VENOM_OF_THE_BROODMOTHER:
                break;
            case MEGA_HAMMER:
                this.megaHammer();
                break;
            case POETS_QUILL:
                // requires a trinket parse to determine its effect for other players
                break;
            // all other gifts are one-off and fully handled by separate chat messages or gui screens
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Name=").append(name).append(", Grave=").append(graveTimer).append("s").append("\n");
        s.append("Specs=").append(specs).append("\n");
        s.append("Aspect=").append(aspect).append("\n");
        s.append("Curses=").append(curses).append("\n");
        s.append("Passives=").append(passives).append("\n");
        s.append("Actives={");
        if (actives != null) {
            for (Map.Entry<ActiveSlot, Active> e : actives.entrySet()) {
                s.append("\n    ").append(e.getKey()).append("={").append(e.getValue()).append("}");
            }
        }
        s.append("\n}\n");
        s.append("Wildcards=").append(wildcards);

        return s.toString();
    }
}