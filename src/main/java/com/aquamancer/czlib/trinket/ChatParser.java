package com.aquamancer.czlib.trinket;

import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApiStatus.Internal
public class ChatParser {
    private static final Pattern ABILITY_REGEX = Pattern.compile("^\\[Zenith Party] (\\w+) (now has|now have|upgraded|downgraded|has lost|lost) ability: (.*?)(?:( (?:at|to) (\\w+) level!)|!)");
    public static void onChatMessage(Text message) {
        if (!ShardTracker.isInZenith()) return;
        String line = message.getString();

    }

    private static boolean parseAbility(String line) {
        Matcher matcher = ABILITY_REGEX.matcher(line);
        if (!matcher.matches()) return false;

        String player = matcher.group(1);
        String verb = matcher.group(2);
        String ability = matcher.group(3);
        String rarity = matcher.group(4);

        switch (verb) {
            case "now has":
            case "now have":
            case "upgraded":
            case "downgraded":

        }
        return false;
    }
}
