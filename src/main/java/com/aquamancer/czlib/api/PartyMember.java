package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;

import java.util.*;

public class PartyMember {
    private final String name;
    private double graveTimer;
    private EnumSet<Spec> specs = EnumSet.noneOf(Spec.class);
    private Set<Passive> passives = new HashSet<>();
    private EnumSet<Curse> curses = EnumSet.noneOf(Curse.class);
    private Aspect aspect;
    private EnumMap<ActiveSlot, Active> actives = new EnumMap<>(ActiveSlot.class);
    private Set<Active> wildcards = new HashSet<>();

    public PartyMember(String name) {
        this.name = name;
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

    public void setAbility(Passive passive) {
        this.passives.add(passive);
    }

    public void setAbility(Curse curse) {
        this.curses.add(curse);
    }

    public void setAbility(Active active) {
        if (active.getSlot() == ActiveSlot.WILDCARD) {
            this.wildcards.add(active);
        } else {
            this.actives.put(active.getSlot(), active);
        }
    }

    public void loseAbility(Passives passive) {
        this.passives.remove(new Passive(passive, null, null));
    }

    public void loseAbility(Curse curse) {
        this.curses.remove(curse);
    }

    public void loseAbility(ActiveType active) {
        actives.values().removeIf(e -> e.getAbility() == active);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Name=").append(name).append(", Grave=").append(graveTimer).append("s").append("\n");
        s.append("Specs=").append(specs).append("\n");
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