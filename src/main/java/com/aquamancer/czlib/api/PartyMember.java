package com.aquamancer.czlib.api;

import com.aquamancer.czlib.Czlib;
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
    private EnumMap<Actives.Wildcard, Active> wildcards = new EnumMap<>(Actives.Wildcard.class);
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
        double old = this.graveTimer;
        this.graveTimer = time;

        if (this.graveTimer != old) {
            ZenithApiUpdateEvents.GRAVE_TIMER.invoker().onGraveTimerUpdate(this.name);
        }
    }

    void setPassives(EnumMap<Passives, Passive> passives) {
        EnumMap<Passives, Passive> old = this.passives;
        this.passives = passives;

        boolean changed = this.passives.size() != old.size()
                || this.passives.entrySet().stream().anyMatch((entry) -> {
                    Passive other = old.get(entry.getKey());
                    return other == null || !entry.getValue().deepEquals(other);
                });
        if (changed) {
            ZenithApiUpdateEvents.PASSIVE.invoker().onPassiveUpdate(this.name);
        }
    }

    void setCurses(EnumSet<Curse> curses) {
        EnumSet<Curse> old = this.curses;
        this.curses = curses;

        if (!this.curses.equals(old)) {
            ZenithApiUpdateEvents.CURSE.invoker().onCurseUpdate(this.name);
        }
    }

    void setSpecs(EnumSet<Spec> specs) {
        EnumSet<Spec> old = this.specs;
        this.specs = specs;

        if (!this.specs.equals(old)) {
            ZenithApiUpdateEvents.SPEC.invoker().onSpecUpdate(this.name);
        }
    }

    void setAspect(Aspect aspect) {
        Aspect old = this.aspect;
        this.aspect = aspect;

        if (this.aspect != old) {
            ZenithApiUpdateEvents.ASPECT.invoker().onAspectUpdate(this.name);
        }
    }

    public void setActives(List<Active> actives) {
        EnumMap<ActiveSlot, Active> nonWildcards = new EnumMap<>(ActiveSlot.class);
        EnumMap<Actives.Wildcard, Active> wildcards = new EnumMap<>(Actives.Wildcard.class);
        for (Active active : actives) {
            ActiveSlot slot = active.getSlot();
            if (slot == ActiveSlot.WILDCARD && active.getAbility() instanceof Actives.Wildcard wildcard) {
                if (active.getAbility() == Actives.Wildcard.CONVERGENCE) {
                    // do not replace other existing wildcards if convergence is present
                    // gui/chat will be the source of rarities
                    wildcards.putAll(this.wildcards);
                }
                wildcards.put(wildcard, active);
            } else {
                nonWildcards.put(active.getSlot(), active);
            }
        }

        EnumMap<ActiveSlot, Active> oldActives = this.actives;
        EnumMap<Actives.Wildcard, Active> oldWildcards = this.wildcards;
        this.actives = nonWildcards;
        this.wildcards = wildcards;

        boolean changed = this.actives.size() != oldActives.size()
                || this.wildcards.size() != oldWildcards.size()
                || this.actives.entrySet().stream().anyMatch((entry) -> {
                    Active other = oldActives.get(entry.getKey());
                    return other == null || !entry.getValue().deepEquals(other);
                })
                || this.wildcards.entrySet().stream().anyMatch((entry) -> {
                    Active other = oldWildcards.get(entry.getKey());
                    return other == null || !entry.getValue().deepEquals(other);
                });
        if (changed) {
            ZenithApiUpdateEvents.ACTIVE.invoker().onActiveUpdate(this.name);
        }

    }

    void addAbility(Passive passive) {
        Passive old = this.passives.put(passive.getAbility(), passive);

        if (old == null || !old.deepEquals(passive)) {
            ZenithApiUpdateEvents.PASSIVE.invoker().onPassiveUpdate(this.name);
        }
    }

    void addAbility(Curse curse) {
        if (this.curses.add(curse)) {
            ZenithApiUpdateEvents.CURSE.invoker().onCurseUpdate(this.name);
        }
    }

    void addAbility(Active active) {
        if (active.getSlot() == ActiveSlot.WILDCARD && active.getAbility() instanceof Actives.Wildcard wildcard) {
            Active old = this.wildcards.put(wildcard, active);
            if (old == null || !old.deepEquals(active)) {
                ZenithApiUpdateEvents.ACTIVE.invoker().onActiveUpdate(this.name);
            }
        } else {
            Active old = this.actives.put(active.getSlot(), active);
            if (old == null || !old.deepEquals(active)) {
                ZenithApiUpdateEvents.ACTIVE.invoker().onActiveUpdate(this.name);
            }
        }
    }

    void loseAbility(Passives passive) {
        Passive old = this.passives.remove(passive);
        if (old != null) {
            ZenithApiUpdateEvents.PASSIVE.invoker().onPassiveUpdate(this.name);
        }
    }

    void loseAbility(Curse curse) {
        if (this.curses.remove(curse)) {
            ZenithApiUpdateEvents.CURSE.invoker().onCurseUpdate(this.name);
        }
    }

    void loseAbility(ActiveType active) {
        if (actives.values().removeIf(e -> e.getAbility() == active)) {
            ZenithApiUpdateEvents.ACTIVE.invoker().onActiveUpdate(this.name);
            return;
        }

        if (active instanceof Actives.Wildcard) {
            if (active == Actives.Wildcard.CONVERGENCE) {
                wildcards.clear();
                ZenithApiUpdateEvents.ACTIVE.invoker().onActiveUpdate(this.name);
            } else {
                if (wildcards.remove(active) != null) {
                    ZenithApiUpdateEvents.ACTIVE.invoker().onActiveUpdate(this.name);
                }
            }
        }
    }

    void addSpec(Spec spec) {
        if (this.specs.add(spec)) {
            ZenithApiUpdateEvents.SPEC.invoker().onSpecUpdate(this.name);
        }
    }

    void invertSpecs() {
        this.specs = EnumSet.complementOf(this.specs);

        if (!this.specs.equals(EnumSet.allOf(Spec.class))) {
            ZenithApiUpdateEvents.SPEC.invoker().onSpecUpdate(this.name);
        }
    }

    private void replaceAll(Function<Active, Active> newActive, Function<Passive, Passive> newPassive) {
        this.actives.replaceAll((k, v) -> newActive.apply(v));
        this.wildcards.replaceAll((k, v) -> newActive.apply(v));
        this.passives.replaceAll((k, v) -> newPassive.apply(v));

        ZenithApiUpdateEvents.ACTIVE.invoker().onActiveUpdate(this.name);
        ZenithApiUpdateEvents.PASSIVE.invoker().onPassiveUpdate(this.name);
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
                ZenithApiUpdateEvents.GIFT.invoker().onGiftUpdate(this.name);
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