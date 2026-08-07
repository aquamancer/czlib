package com.aquamancer.czlib.api.abils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Gifts implements Ability {
    TWISTED_SCROLL("Twisted Scroll"),
    FORSAKEN_GRIMOIRE("Forsaken Grimoire"),
    PRISMATIC_CUBE("Prismatic Cube"),
    NORTHERN_STAR("Northern Star", 4),
    BOTTOMLESS_BOWL("Bottomless Bowl"),
    POETS_QUILL("Poet's Quill"),
    PURGING_STONE("Purging Stone"),
    WILD_CARD("Wild Card"),
    AVARICIOUS_PENDANT("Avaricious Pendant"),
    CELESTIAL_SURPRISE("Celestial Surprise"),
    COMB_OF_SELECTION("Comb of Selection"),
    PILLAR_OF_LIGHT("Pillar of Light"),
    BROKEN_CLOCK("Broken Clock"),
    TREASURE_MAP("Treasure Map"),
    MEGA_HAMMER("Mega Hammer"),
    KALEIDOSCOPIC_LENS("Kaleidoscopic Lens"),
    CALLICARPAS_POINTED_HAT("Callicarpa's Pointed Hat", 3),
    VENOM_OF_THE_BROODMOTHER("Venom of the Broodmother"),
    BROODMOTHERS_WEBBING("Broodmother's Webbing"),
    STATUE_OF_REGRET("Statue of Regret"),
    RAINBOW_GEODE("Rainbow Geode", 3),
    CRACKED_IDOL("Cracked Idol", 1),
    ORB_OF_DARKNESS("Orb of Darkness");

    private final String displayName;
    private final int defaultCounter;

    Gifts(String displayName) {
        this(displayName, 0);
    }

    Gifts(String displayName, int defaultCounter) {
        this.displayName = displayName;
        this.defaultCounter = defaultCounter;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDefaultValue() {
        return defaultCounter;
    }

    @Override
    public Enum<?> getAbility() {
        return this;
    }

    private static final Map<String, Gifts> fromString =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Gifts::getDisplayName, Function.identity()));

    public static Optional<Gifts> fromString(String string) {
        return Optional.ofNullable(fromString.get(string));
    }
}
