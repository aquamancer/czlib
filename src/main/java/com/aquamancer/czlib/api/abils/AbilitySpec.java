package com.aquamancer.czlib.api.abils;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AbilitySpec {
    DAWN("Dawnbringer", Spec.DAWN),
    EARTH("Earthbound", Spec.EARTH),
    FLAME("Flamecaller", Spec.FLAME),
    FROST("Frostborn", Spec.FROST),
    SHADOW("Shadowdancer", Spec.SHADOW),
    STEEL("Steelsage", Spec.STEEL),
    WIND("Windwalker", Spec.WIND),
    PRISMATIC("Prismatic", null);

    private final String displayName;
    private final Spec spec;

    AbilitySpec(String displayName, Spec spec) {
        this.displayName = displayName;
        this.spec = spec;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Optional<Spec> toSpec() {
        return Optional.ofNullable(spec);
    }

    public int getColor() {
        if (this.spec == null) {
            return 0xff9cf0;  // prismatic
        } else {
            return this.spec.getColor();
        }
    }

    private static final Map<String, AbilitySpec> fromString = Arrays.stream(values())
            .collect(Collectors.toMap(AbilitySpec::getDisplayName, Function.identity()));

    private static final Map<Spec, AbilitySpec> fromSpec = new EnumMap<>(Spec.class);
    static {
        for (Spec spec : Spec.values()) {
            for (AbilitySpec abilitySpec : AbilitySpec.values()) {
                if (abilitySpec.spec == spec) {
                    fromSpec.put(spec, abilitySpec);
                    break;
                }
            }
        }
        if (fromSpec.size() != Spec.values().length) {
            throw new RuntimeException("Not every Spec is mapped to a corresponding AbilitySpec");
        }
    }

    private static final Map<AbilitySpec, EnumSet<Actives>> actives = new EnumMap<>(AbilitySpec.class);
    static {
        for (AbilitySpec spec : AbilitySpec.values()) {
            actives.put(spec, EnumSet.noneOf(Actives.class));
        }
        for (Actives active : Actives.values()) {
            actives.get(active.getSpec()).add(active);
        }
    }

    private static final Map<AbilitySpec, EnumSet<Passives>> passives = new EnumMap<>(AbilitySpec.class);
    static {
        for (AbilitySpec spec : AbilitySpec.values()) {
            passives.put(spec, EnumSet.noneOf(Passives.class));
        }
        for (Passives passive : Passives.values()) {
            passives.get(passive.getSpec()).add(passive);
        }
    }

    public static Optional<AbilitySpec> fromString(String string) {
        if (string == null) return Optional.empty();
        return Optional.ofNullable(fromString.get(string));
    }

    public static AbilitySpec fromSpec(Spec spec) {
        return fromSpec.get(spec);
    }

    public static Optional<AbilitySpec> fromAbilityName(String ability) {
        return Actives.getSpec(ability).or(() -> Passives.getSpec(ability));
    }

    public static EnumSet<Actives> getActives(AbilitySpec spec) {
        return EnumSet.copyOf(actives.get(spec));
    }

    public static EnumSet<Passives> getPassives(AbilitySpec spec) {
        return EnumSet.copyOf(passives.get(spec));
    }

    public static Set<Ability<?>> getAllAbilities(AbilitySpec spec) {
        Set<Ability<?>> all = new LinkedHashSet<>(getActives(spec));
        all.addAll(getPassives(spec));
        return all;
    }

    public static Set<Ability<?>> getAllAbilities(
            AbilitySpec spec,
            @Nullable Comparator<Actives> activeSorter,
            @Nullable Comparator<Passives> passiveSorter,
            boolean activesFirst) {

        List<Actives> actives = new ArrayList<>(getActives(spec));
        if (activeSorter != null) {
            actives.sort(activeSorter);
        }
        List<Passives> passives = new ArrayList<>(getPassives(spec));
        if (passiveSorter != null) {
            passives.sort(passiveSorter);
        }

        Set<Ability<?>> result;
        if (activesFirst) {
            result = new LinkedHashSet<>(actives);
            result.addAll(passives);
        } else {
            result = new LinkedHashSet<>(passives);
            result.addAll(actives);
        }
        return result;
    }

    public static class AbilitySpecComparator implements Comparator<HasAbilitySpec> {
        private final Map<AbilitySpec, Integer> priority;
        public AbilitySpecComparator(Map<AbilitySpec, Integer> priority) {
            this.priority = priority;
        }

        @Override
        public int compare(HasAbilitySpec o1, HasAbilitySpec o2) {
            Integer p1 = priority.get(o1.getSpec());
            Integer p2 = priority.get(o2.getSpec());
            if (p1 == null && p2 == null) return 0;
            if (p1 == null) return -1;
            if (p2 == null) return 1;
            return Integer.compare(p1, p2);
        }
    }
}
