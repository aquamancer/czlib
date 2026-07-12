package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;
import com.aquamancer.czlib.api.abils.Gifts;
import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import com.aquamancer.czlib.api.event.ZenithApiUpdateEvents;
import com.aquamancer.czlib.api.rooms.Rooms;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PartyMember {
    private final String name;
    private double graveTimer;
    private EnumSet<Spec> specs = EnumSet.noneOf(Spec.class);
    private EnumMap<Passives, Passive> passives = new EnumMap<>(Passives.class);
    private EnumSet<Curse> curses = EnumSet.noneOf(Curse.class);
    private Aspect aspect;
    private EnumMap<ActiveSlot, Active> actives = new EnumMap<>(ActiveSlot.class);
    private Set<Active> wildcards = new HashSet<>();
    private EnumMap<Gifts, Gift> gifts = new EnumMap<>(Gifts.class);

    public PartyMember(String name) {
        this.name = name;

        ZenithApiStateEvents.ROOM_SPAWNED.register((room, wildcard) -> {
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
        if (this.graveTimer != time) {
            ZenithApiUpdateEvents.GRAVE_TIMER.invoker().onGraveTimerUpdate(this.name);
        }
        this.graveTimer = time;
    }

    void setPassives(EnumMap<Passives, Passive> passives) {
        boolean changed = this.passives.size() != passives.size()
                || this.passives.entrySet().stream().anyMatch((entry) -> {
                    Passive other = passives.get(entry.getKey());
                    return other == null || !entry.getValue().deepEquals(other);
                });
        if (changed) {
            ZenithApiUpdateEvents.PASSIVE.invoker().onPassiveUpdate(this.name);
        }

        this.passives = passives;
    }

    void setCurses(EnumSet<Curse> curses) {
        if (!this.curses.equals(curses)) {
            ZenithApiUpdateEvents.CURSE.invoker().onCurseUpdate(this.name);
        }
        this.curses = curses;
    }

    void setSpecs(EnumSet<Spec> specs) {
        if (!this.specs.equals(specs)) {
            ZenithApiUpdateEvents.SPEC.invoker().onSpecUpdate(this.name);
        }
        this.specs = specs;
    }

    void setAspect(Aspect aspect) {
        if (this.aspect != aspect) {
            ZenithApiUpdateEvents.ASPECT.invoker().onAspectUpdate(this.name);
        }
        this.aspect = aspect;
    }

    public void setActives(List<Active> actives) {
        EnumMap<ActiveSlot, Active> nonWildcards = new EnumMap<>(ActiveSlot.class);
        Set<Active> wildcards = new HashSet<>();
        for (Active active : actives) {
            ActiveSlot slot = active.getSlot();
            if (slot == ActiveSlot.WILDCARD) {
                wildcards.add(active);
            } else {
                nonWildcards.put(slot, active);
            }
        }

        this.actives = nonWildcards;
        this.wildcards = wildcards;
    }

    void addAbility(Passive passive) {
        this.passives.put(passive.getAbility(), passive);
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
        this.passives.replaceAll((k, v) -> newPassive.apply(v));
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
        s.append("Actives=");
        if (actives != null) {
            s.append("{");
            for (Map.Entry<ActiveSlot, Active> e : actives.entrySet()) {
                s.append("\n    ").append(e.getKey()).append("={").append(e.getValue()).append("}");
            }
            s.append("\n}\n");
        } else {
            s.append("null\n");
        }
        s.append("Wildcards=").append(wildcards);

        return s.toString();
    }
}