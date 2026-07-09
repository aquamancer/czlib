package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;
import com.aquamancer.czlib.api.abils.Gifts;

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
    private Set<Gift> gifts = new HashSet();

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

    void downgradeAll() {
        Collection<Active> oldActives = List.copyOf(this.actives.values());
        this.actives.clear();
        for (Active old : oldActives) {
            Active replacement = new Active(old.getAbility(), old.getSpec(), Rarity.downgrade(old.getRarity()));
            if (old.getSlot() == ActiveSlot.WILDCARD) {
                this.wildcards.add(replacement);
            } else {
                this.actives.put(replacement.getSlot(), replacement);
            }
        }

        Collection<Passive> oldPassives = List.copyOf(this.passives);
        this.passives.clear();
        for (Passive old : oldPassives) {
            this.passives.add(new Passive(old.getAbility(), old.getSpec(), Rarity.downgrade(old.getRarity())));
        }
    }

    void megaHammer() {
        Collection<Active> oldActives = List.copyOf(this.actives.values());
        this.actives.clear();
        for (Active old : oldActives) {
            Active replacement;
            if (old.getRarity() == Rarity.COMMON || old.getRarity() == Rarity.UNCOMMON) {
                replacement = new Active(old.getAbility(), old.getSpec(), Rarity.EPIC);
            } else {
                replacement = old;
            }

            if (old.getSlot() == ActiveSlot.WILDCARD) {
                this.wildcards.add(replacement);
            } else {
                this.actives.put(replacement.getSlot(), replacement);
            }
        }

        Collection<Passive> oldPassives = List.copyOf(this.passives);
        this.passives.clear();
        for (Passive old : oldPassives) {
            Passive replacement;
            if (old.getRarity() == Rarity.COMMON || old.getRarity() == Rarity.UNCOMMON) {
                replacement = new Passive(old.getAbility(), old.getSpec(), Rarity.EPIC);
            } else {
                replacement = old;
            }
            this.passives.add(replacement);
        }
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
                this.gifts.add(new Gift(gift, Gifts.getDefaultValue(gift)));
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