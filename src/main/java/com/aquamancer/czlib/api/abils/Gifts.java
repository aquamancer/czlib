package com.aquamancer.czlib.api.abils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Gifts {
    TWISTED_SCROLL,
    FORSAKEN_GRIMOIRE,
    PRISMATIC_CUBE,
    NORTHERN_STAR,
    BOTTOMLESS_BOWL,
    POETS_QUILL,
    PURGING_STONE,
    WILD_CARD,
    AVARICIOUS_PENDANT,
    CELESTIAL_SURPRISE,
    COMB_OF_SELECTION,
    PILLAR_OF_LIGHT,
    BROKEN_CLOCK,
    TREASURE_MAP,
    MEGA_HAMMER,
    KALEIDOSCOPIC_LENS,
    CALLICARPAS_POINTED_HAT,
    VENOM_OF_THE_BROODMOTHER,
    BROODMOTHERS_WEBBING,
    STATUE_OF_REGRET,
    RAINBOW_GEODE,
    CRACKED_IDOL,
    ORB_OF_DARKNESS;

    private static final Map<String, Gifts> fromString = new HashMap<>();
    private static final Map<Gifts, Integer> defaultCounters = new HashMap<>();

    public static Optional<Gifts> toEnum(String string) {
        return Optional.ofNullable(fromString.get(string));
    }

    public static int getDefaultValue(Gifts gift) {
        Integer v = defaultCounters.get(gift);
        return (v == null) ? 0 : v;
    }

    static {
        fromString.put("Twisted Scroll", TWISTED_SCROLL);
        fromString.put("Forsaken Grimoire", FORSAKEN_GRIMOIRE);
        fromString.put("Prismatic Cube", PRISMATIC_CUBE);
        fromString.put("Northern Star", NORTHERN_STAR);
        fromString.put("Bottomless Bowl", BOTTOMLESS_BOWL);
        fromString.put("Poet's Quill", POETS_QUILL);
        fromString.put("Purging Stone", PURGING_STONE);
        fromString.put("Wild Card", WILD_CARD);
        fromString.put("Avaricious Pendant", AVARICIOUS_PENDANT);
        fromString.put("Celestial Surprise", CELESTIAL_SURPRISE);
        fromString.put("Comb of Selection", COMB_OF_SELECTION);
        fromString.put("Pillar of Light", PILLAR_OF_LIGHT);
        fromString.put("Broken Clock", BROKEN_CLOCK);
        fromString.put("Treasure Map", TREASURE_MAP);
        fromString.put("Mega Hammer", MEGA_HAMMER);
        fromString.put("Kaleidoscopic Lens", KALEIDOSCOPIC_LENS);
        fromString.put("Callicarpa's Pointed Hat", CALLICARPAS_POINTED_HAT);
        fromString.put("Venom of the Broodmother", VENOM_OF_THE_BROODMOTHER);
        fromString.put("Broodmother's Webbing", BROODMOTHERS_WEBBING);
        fromString.put("Statue of Regret", STATUE_OF_REGRET);
        fromString.put("Rainbow Geode", RAINBOW_GEODE);
        fromString.put("Cracked Idol", CRACKED_IDOL);
        fromString.put("Orb of Darkness", ORB_OF_DARKNESS);

        defaultCounters.put(NORTHERN_STAR, 4);
        defaultCounters.put(CALLICARPAS_POINTED_HAT, 3);
        defaultCounters.put(RAINBOW_GEODE, 3);
    }
}
