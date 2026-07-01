package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

public class PartyMember {
    private final String name;
    private double graveTimer;
    private EnumSet<Spec> specs;
    private List<Passive> passives;
    private EnumSet<Curse> curses;
    private Aspect aspect;
    private EnumMap<ActiveSlot, Active> actives;
    private List<Active> wildcards;

    public PartyMember(String name) {
        this.name = name;
    }

    void setGraveTimer(double time) {
        this.graveTimer = time;
    }

    void setPassives(List<Passive> passives) {
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

    public void setWildcards(List<Active> wildcards) {
        this.wildcards = wildcards;
    }
}