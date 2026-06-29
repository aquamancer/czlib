package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;

import java.util.EnumSet;
import java.util.List;

public class PartyMember {
    private String name;
    private double graveTimer;
    private EnumSet<Spec> specs;
    private List<Passive> passives;
    private EnumSet<Curse> curses;
    private Aspect aspect;
    private Active combo;
    private Active right;
    private Active leftShift;
    private Active rightShift;
    private List<Active> wildcards;
    private Active bow;
    private Active lifeline;
}