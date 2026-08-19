package com.aquamancer.czlib.api.screens;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ZenithScreens {
    TRINKET("Current Abilities"),

    TREE("Select a Tree"),
    ROOM("Select the Next Room Type"),
    CHARM_POWER("Zenith Charm Power"),

    ASPECT("Select an Aspect"),
    ABILITY("Select an Ability"),
    UPGRADE("Select an Upgrade"),
    CLEANSE("Remove an Ability"),
    MUTATE("Mutate an Ability Trigger"),
    GENEROSITY("Accept or Reject the Gift"),

    WEBBING("Webbing (Select Player)"),
    POINTED_HAT("Pointed Hat (Select Tree)"),
    GRIMOIRE_TREE("Grimoire (Select Tree)"),
    GRIMOIRE_ABILITY("Grimoire (Select Ability)"),
    QUILL_REMOVE("Poet's Quill (Remove Tree)"),
    QUILL_REPLACE("Poet's Quill (Replace Tree)"),
    PRISMATIC_CUBE("Prismatic Cube (Replace)"),
    STATUE_OF_REGRET_REMOVE("Regret (Remove Curse)"),
    STATUE_OF_REGRET_ADD("Regret (Replace Curse)"),

    DEPTHS_DEBUG("Depths Debug GUI");

    private final String title;

    ZenithScreens(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    private static final Map<String, ZenithScreens> fromString =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            ZenithScreens::getTitle,
                            Function.identity()
                    ));

    public static Optional<ZenithScreens> fromString(String string) {
        if (string == null) return Optional.empty();
        return Optional.ofNullable(fromString.get(string));
    }
}