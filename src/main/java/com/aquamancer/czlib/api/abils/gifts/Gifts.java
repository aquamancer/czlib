package com.aquamancer.czlib.api.abils.gifts;

import com.aquamancer.czlib.api.abils.Ability;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Gifts implements Ability<Gifts> {
    TWISTED_SCROLL("Twisted Scroll"),
    FORSAKEN_GRIMOIRE("Forsaken Grimoire"),
    PRISMATIC_CUBE("Prismatic Cube"),
    NORTHERN_STAR("Northern Star", 4),
    BOTTOMLESS_BOWL("Bottomless Bowl", true),
    POETS_QUILL("Poet's Quill"),
    PURGING_STONE("Purging Stone"),
    WILD_CARD("Wild Card", true),
    AVARICIOUS_PENDANT("Avaricious Pendant", true),
    CELESTIAL_SURPRISE("Celestial Surprise"),
    COMB_OF_SELECTION("Comb of Selection", true),
    PILLAR_OF_LIGHT("Pillar of Light", true),
    BROKEN_CLOCK("Broken Clock", true),
    TREASURE_MAP("Treasure Map", true),
    MEGA_HAMMER("Mega Hammer"),
    KALEIDOSCOPIC_LENS("Kaleidoscopic Lens"),
    CALLICARPAS_POINTED_HAT("Callicarpa's Pointed Hat", 3),
    VENOM_OF_THE_BROODMOTHER("Venom of the Broodmother"),
    BROODMOTHERS_WEBBING("Broodmother's Webbing"),
    STATUE_OF_REGRET("Statue of Regret"),
    RAINBOW_GEODE("Rainbow Geode", true),
    CRACKED_IDOL("Cracked Idol", true),
    ORB_OF_DARKNESS("Orb of Darkness");

    public static final int COLOR = 0x25f6f5;

    private final String displayName;
    private final int defaultCounter;
    private final boolean isPersistent;

    Gifts(String displayName) {
        this.displayName = displayName;
        this.defaultCounter = 0;
        this.isPersistent = false;
    }

    Gifts(String displayName, boolean isPersistent) {
        this.displayName = displayName;
        this.defaultCounter = 0;
        this.isPersistent = isPersistent;
    }

    Gifts(String displayName, int defaultCounter) {
        this.displayName = displayName;
        this.defaultCounter = defaultCounter;
        // assume isPersistent is true if defaultCounter is manually set
        this.isPersistent = true;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDefaultValue() {
        return defaultCounter;
    }

    public boolean isPersistent() {
        return isPersistent;
    }

    @Override
    public Gifts getAbility() {
        return this;
    }

    @Override
    public MutableText getText() {
        return Text.literal(getDisplayName()).withColor(COLOR);
    }

    private static final Map<String, Gifts> fromString =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Gifts::getDisplayName, Function.identity()));

    public static Optional<Gifts> from(String string) {
        if (string == null) return Optional.empty();
        return Optional.ofNullable(fromString.get(string));
    }

    public static Optional<Gifts> from(List<Text> tooltip) {
        if (tooltip.isEmpty()) return Optional.empty();
        return from(tooltip.get(0).getString());
    }

    public static Optional<Gifts> from(ItemStack item) {
        if (item == null) return Optional.empty();
        return from(item.getName().getString());
    }
}
