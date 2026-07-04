package com.aquamancer.czlib.trinket;

import com.aquamancer.czlib.api.Party;
import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.api.abils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApiStatus.Internal
public class ChatParser {
    private static final Pattern ABILITY_REGEX = Pattern.compile("^\\[Zenith Party] (\\w+) (now has|now have|upgraded|downgraded|has lost|lost) ability: (.*?)(?: (?:at|to) (\\w+) level!|!)");

    public static void onChatMessage(Text message) {
        if (ShardTracker.notInZenith()) return;
        String line = message.getString();
        parseAbility(line);
    }

    private static boolean parseAbility(String line) {
        Matcher matcher = ABILITY_REGEX.matcher(line);
        if (!matcher.matches()) return false;

        String player = matcher.group(1);
        if (player.equals("You")) {
            player = SelfIdentifier.getSelfName();
        }
        String verb = matcher.group(2);
        String ability = matcher.group(3);
        Optional<Rarity> rarity = Rarity.toEnum(matcher.group(4));
        Optional<AbilitySpec> spec = AbilitySpec.fromAbilityName(ability);


        Optional<Passives> passive = Passives.toEnum(ability);
        Optional<Curse> curse = (passive.isPresent()) ? Optional.empty() : Curse.toEnum(ability);
        Optional<? extends ActiveType> active = (curse.isPresent()) ? Optional.empty() : Actives.toEnum(ability);

        Party party = ZenithApi.getInstance().getPartyManager();
        party.createMember(player);
        switch (verb) {
            case "now has":
            case "now have":
            case "upgraded":
            case "downgraded":
                if (passive.isPresent() && spec.isPresent() && rarity.isPresent()) {
                    party.updateAbility(player, new Passive(passive.get(), spec.get(), rarity.get()));
                } else if (curse.isPresent()) {
                    party.updateAbility(player, curse.get());
                } else if (active.isPresent() && spec.isPresent() && rarity.isPresent()) {
                    party.updateAbility(player, new Active(active.get(), spec.get(), rarity.get()));
                }
                break;
            case "has lost":
            case "lost":
                if (passive.isPresent()) {
                    party.loseAbility(player, passive.get());
                } else if (curse.isPresent()) {
                    party.loseAbility(player, curse.get());
                } else if (active.isPresent()) {
                    party.loseAbility(player, active.get());
                }
                break;
        }
        return true;
    }
}
