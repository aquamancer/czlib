package com.aquamancer.czlib.api;

import com.aquamancer.czlib.api.abils.*;
import com.aquamancer.czlib.api.event.ZenithApiUpdateEvents;
import com.aquamancer.czlib.api.rooms.Rooms;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PartyMember {
    private AbstractClientPlayerEntity entity;

    private final String name;
    private double graveTimer;

    private EnumSet<Spec> specs = EnumSet.noneOf(Spec.class);
    private EnumMap<Passives, Passive> passives = new EnumMap<>(Passives.class);
    private EnumSet<Curse> curses = EnumSet.noneOf(Curse.class);
    private Aspect aspect;
    private EnumMap<Actives, Active> actives = new EnumMap<>(Actives.class);
    private EnumMap<Gifts, Gift> gifts = new EnumMap<>(Gifts.class);
    private EnumMap<Spec, Integer> charmLines = new EnumMap<>(Spec.class);

    public PartyMember(String name) {
        this.name = name;
    }

    void onRoomSpawned(Rooms room, boolean wildcard) {
        Gift northernStar = gifts.computeIfPresent(Gifts.NORTHERN_STAR, (k, v) -> {
            if (room == Rooms.ABILITY_ELITE || room == Rooms.UPGRADE_ELITE) {
                v.decrement();
                ZenithApiUpdateEvents.GIFT.invoker().onUpdate(this);
            }
            return v;
        });
        if (northernStar != null) {
            if (northernStar.getCounter() <= 0) {
                gifts.remove(Gifts.NORTHERN_STAR);
            }
            ZenithApiUpdateEvents.GIFT.invoker().onUpdate(this);
        }

        gifts.computeIfPresent(Gifts.WILD_CARD, (k, v) -> {
            if (wildcard) {
                v.increment();
                ZenithApiUpdateEvents.GIFT.invoker().onUpdate(this);
            }
            return v;
        });

        Gift hat = gifts.computeIfPresent(Gifts.CALLICARPAS_POINTED_HAT, (k, v) -> {
            v.decrement();
            return v;
        });
        if (hat != null) {
            if (hat.getCounter() <= 0) {
                gifts.remove(Gifts.CALLICARPAS_POINTED_HAT);
            }
            ZenithApiUpdateEvents.GIFT.invoker().onUpdate(this);
        }
    }

    void onDeath() {
        if (gifts.remove(Gifts.CRACKED_IDOL) != null) {
            ZenithApiUpdateEvents.GIFT.invoker().onUpdate(this);
        }
    }

    void setEntity(AbstractClientPlayerEntity entity) {
        this.entity = entity;
    }

    void setGraveTimer(double time) {
        double old = this.graveTimer;
        this.graveTimer = time;

        if (this.graveTimer != old) {
            ZenithApiUpdateEvents.GRAVE_TIMER.invoker().onUpdate(this);
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
            ZenithApiUpdateEvents.PASSIVE.invoker().onUpdate(this);
        }
    }

    void setCurses(EnumSet<Curse> curses) {
        EnumSet<Curse> old = this.curses;
        this.curses = curses;

        if (!this.curses.equals(old)) {
            ZenithApiUpdateEvents.CURSE.invoker().onUpdate(this);
        }
    }

    void setSpecs(EnumSet<Spec> specs) {
        EnumSet<Spec> old = this.specs;
        this.specs = specs;

        if (!this.specs.equals(old)) {
            ZenithApiUpdateEvents.SPEC.invoker().onUpdate(this);
        }
    }

    void setAspect(Aspect aspect) {
        Aspect old = this.aspect;
        this.aspect = aspect;

        if (this.aspect != old) {
            ZenithApiUpdateEvents.ASPECT.invoker().onUpdate(this);
        }
    }

    public void setActives(Set<Active> actives) {
        EnumMap<Actives, Active> newActives = new EnumMap<>(Actives.class);
        if (this.actives.containsKey(Actives.CONVERGENCE)) {
            // retain current wildcards
            this.actives.forEach((k, v) -> {
                if (k.getSlot() == ActiveSlot.WILDCARD) {
                    newActives.put(k, v);
                }
            });
        }
        for (Active active : actives) {
            newActives.put(active.getAbility(), active);
        }

        EnumMap<Actives, Active> oldActives = this.actives;
        this.actives = newActives;

        boolean changed = this.actives.size() != oldActives.size()
                || this.actives.entrySet().stream().anyMatch((entry) -> {
                    Active other = oldActives.get(entry.getKey());
                    return other == null || !entry.getValue().deepEquals(other);
                });
        if (changed) {
            ZenithApiUpdateEvents.ACTIVE.invoker().onUpdate(this);
        }
    }

    void addAbility(Passive passive) {
        Passive old = this.passives.put(passive.getAbility(), passive);

        if (old == null || !old.deepEquals(passive)) {
            ZenithApiUpdateEvents.PASSIVE.invoker().onUpdate(this);
        }
    }

    void addAbility(Curse curse) {
        if (this.curses.add(curse)) {
            ZenithApiUpdateEvents.CURSE.invoker().onUpdate(this);
        }
    }

    void addAbility(Active active) {
        Active replaced = this.actives.put(active.getAbility(), active);
        if (replaced == null || !replaced.deepEquals(active)) {
            ZenithApiUpdateEvents.ACTIVE.invoker().onUpdate(this);
        }
    }

    void loseAbility(Passives passive) {
        Passive old = this.passives.remove(passive);
        if (old != null) {
            ZenithApiUpdateEvents.PASSIVE.invoker().onUpdate(this);
        }
    }

    void loseAbility(Curse curse) {
        if (this.curses.remove(curse)) {
            ZenithApiUpdateEvents.CURSE.invoker().onUpdate(this);
        }
    }

    void loseAbility(Actives active) {
        if (this.actives.remove(active) != null) {
            if (active == Actives.CONVERGENCE) {
                // removing convergence removes all wildcards
                this.actives.entrySet().removeIf(e -> e.getKey().getSlot() == ActiveSlot.WILDCARD);
            }
            ZenithApiUpdateEvents.ACTIVE.invoker().onUpdate(this);
        }
    }

    void addSpec(Spec spec) {
        if (this.specs.add(spec)) {
            ZenithApiUpdateEvents.SPEC.invoker().onUpdate(this);
        }
    }

    void loseSpec(Spec spec) {
        if (this.specs.remove(spec)) {
            ZenithApiUpdateEvents.SPEC.invoker().onUpdate(this);
        }
    }

    void invertSpecs() {
        this.specs = EnumSet.complementOf(this.specs);

        if (!this.specs.equals(EnumSet.allOf(Spec.class))) {
            ZenithApiUpdateEvents.SPEC.invoker().onUpdate(this);
        }
    }

    private void replaceAll(Function<Active, Active> newActive, Function<Passive, Passive> newPassive) {
        this.actives.replaceAll((k, v) -> newActive.apply(v));
        this.passives.replaceAll((k, v) -> newPassive.apply(v));

        ZenithApiUpdateEvents.ACTIVE.invoker().onUpdate(this);
        ZenithApiUpdateEvents.PASSIVE.invoker().onUpdate(this);
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
            case RAINBOW_GEODE:
            case CALLICARPAS_POINTED_HAT:  // other players' tree selection unknown, but self gui will call addGift(Gift) with a tree and replace this
            case CRACKED_IDOL:
                this.gifts.put(gift, new Gift(gift, gift.getDefaultValue()));
                ZenithApiUpdateEvents.GIFT.invoker().onUpdate(this);
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
                // gui handles poet's quill for self
                break;
            // all other gifts are one-off and fully handled by separate chat messages or gui screens
        }
    }

    void addGift(Gift gift) {
        this.gifts.put(gift.getAbility(), gift);
    }

    void setCharmLines(EnumMap<Spec, Integer> charmLines) {
        EnumMap<Spec, Integer> old = this.charmLines;
        this.charmLines = charmLines;

        boolean changed = this.charmLines.size() != old.size()
                || this.charmLines.entrySet().stream().anyMatch((entry) -> {
                    Integer other = old.get(entry.getKey());
                    return !entry.getValue().equals(other);
                });
        if (changed) {
            ZenithApiUpdateEvents.VZC.invoker().onUpdate(this);
        }
    }

    public PlayerEntity getEntity() {
        return this.entity;
    }

    public String getName() {
        return name;
    }

    public double getGraveTimer() {
        return graveTimer;
    }

    public EnumSet<Spec> getSpecs() {
        return specs;
    }

    public Aspect getAspect() {
        return aspect;
    }

    public EnumSet<Curse> getCurses() {
        return curses;
    }

    public Map<Passives, Passive> getPassives() {
        return passives;
    }

    public Map<Passives, Passive> getPassives(AbilitySpec spec) {
        return this.passives.entrySet().stream()
                .filter(e -> e.getKey().getSpec() == spec)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a,
                        () -> new EnumMap<>(Passives.class)
                ));
    }

    public EnumSet<Passives> getPassiveSet(AbilitySpec spec) {
        return EnumSet.copyOf(this.passives.keySet().stream().filter(p -> p.getSpec() == spec).toList());
    }

    public long getPassiveCount(AbilitySpec spec) {
        return this.passives.keySet().stream().filter(p -> p.getSpec() == spec).count();
    }

    public Map<Actives, Active> getActives() {
        return actives;
    }

    public Map<Actives, Active> getActives(AbilitySpec spec) {
        return this.actives.entrySet().stream()
                .filter(e -> e.getKey().getSpec() == spec)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a,
                        () -> new EnumMap<>(Actives.class)
                ));
    }

    public EnumSet<Actives> getActiveSet(AbilitySpec spec) {
        return EnumSet.copyOf(this.actives.keySet().stream().filter(a -> a.getSpec() == spec).toList());
    }

    public long getActiveCount(AbilitySpec spec) {
        return this.actives.keySet().stream().filter(a -> a.getSpec() == spec).count();
    }

    public long getAbilityCount(AbilitySpec spec) {
        return this.getActiveCount(spec) + this.getPassiveCount(spec);
    }

    public Map<Gifts, Gift> getGifts() {
        return gifts;
    }

    public Map<Spec, Integer> getCharmLines() {
        return charmLines;
    }

    public Optional<Spec> getCharmedSpec() {
        return charmLines.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
    }

    public enum BlockReason { NOT_BLOCKED, MORE_THAN_4, SLOT_TAKEN }
    public BlockReason isBlocked(Actives active, boolean a14) {
        if (active.getSlot() == ActiveSlot.SWAP && this.curses.contains(Curse.ANCHORING)) {
            return BlockReason.SLOT_TAKEN;
        } if (active.getSlot() == ActiveSlot.WILDCARD) {
            Active convergence = this.actives.get(Actives.CONVERGENCE);
            if (convergence != null) {
                long numWildcards = this.actives.keySet().stream().filter(a -> a.getSlot() == ActiveSlot.WILDCARD).count();
                if (numWildcards >= Actives.getConvergenceValues(convergence.getRarity())) {
                    return BlockReason.SLOT_TAKEN;
                }
            }
        } else if (this.actives.keySet().stream().anyMatch(a -> a.getSlot() == active.getSlot())) {
            return BlockReason.SLOT_TAKEN;
        }

        if (!a14) return BlockReason.NOT_BLOCKED;
        if (this.actives.keySet().stream().filter(a -> a.getSpec() == active.getSpec()).count() >= 4) {
            return BlockReason.MORE_THAN_4;
        }
        return BlockReason.NOT_BLOCKED;
    }

    public long getGreedAmount() {
        return this.actives.values().stream().filter(a -> a.getRarity().getLevel() >= Rarity.LEGENDARY.getLevel()).count()
                + this.passives.values().stream().filter(p -> p.getRarity().getLevel() >= Rarity.LEGENDARY.getLevel()).count();
    }

    public long getPrideAmount() {
        long count = 0;
        for (AbilitySpec spec : AbilitySpec.values()) {
            if (spec == AbilitySpec.PRISMATIC) continue;
            long abilityCount = getAbilityCount(spec);
            if (abilityCount > 4) {
                count += abilityCount;
            }
        }
        return count;
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
            for (Map.Entry<Actives, Active> e : actives.entrySet()) {
                s.append("\n    ").append(e.getKey()).append("={").append(e.getValue()).append("}");
            }
            s.append("\n}\n");
        } else {
            s.append("null\n");
        }
        s.append("\n").append("Charm lines=").append(charmLines);

        return s.toString();
    }
}