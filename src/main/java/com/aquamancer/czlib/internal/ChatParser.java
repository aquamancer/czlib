package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.api.Party;
import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.api.abils.*;
import com.aquamancer.czlib.api.abils.Gifts;
import com.aquamancer.czlib.api.event.ZenithApiEvents;
import com.aquamancer.czlib.api.rooms.Rooms;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApiStatus.Internal
public class ChatParser {
    private static final Pattern ABILITY = Pattern.compile("^\\[Zenith Party] (\\w+) (now has|now have|upgraded|downgraded|has lost|lost) ability: (.*?)(?: (?:at|to) (\\w+) level!|!)$");
    private static final Pattern ASPECT = Pattern.compile("^\\[Zenith Party] (\\w+) has selected (Mystery Box|Aspect of the (?:Axe|Bow|Scythe|Sword|Wand)) as their aspect!$");
    private static final Pattern ROOM = Pattern.compile("^\\[Zenith Party] Spawned new (Ability|Elite Ability|Upgrade|Elite Upgrade|Utility|Boss) room( \\(Wildcard\\))?!$");
    private static final Pattern TREE_SELECTION = Pattern.compile("^\\[Zenith Party] You have selected the \\w+ tree!$");
    private static final Pattern BOSS_CONQUER = Pattern.compile("^\\[Zenith Party] You received a Celestial Gift for clearing the floor! Check your Trinket to claim the gift.$");
    private static final Pattern NEXT_FLOOR = Pattern.compile("^\\[Zenith Party] Your party earned \\d+ treasure score for clearing floor \\d+! Sending your party to next floor.$");
    private static final Pattern BOSS_CLEANSE_ROOM = Pattern.compile("^\\[Zenith Party] Each player must remove an ability before moving on!$");
    private static final Pattern PURGING_STONE_WHEEL = Pattern.compile("^\\[Zenith Party] (?:Unlucky! )?(\\w+) downgraded all (?:your|their) abilities by a level!$");
    private static final Pattern WHEEL_UPGRADE_2 = Pattern.compile("^\\[Zenith Party] (\\w+) (?:has )?upgraded all (?:your|their) abilities by two levels!$");
    private static final Pattern WHEEL_REROLLS = Pattern.compile("^\\[Zenith Party] (\\w+) gained (\\d+) rerolls!$");
    private static final Pattern WHEEL_SPEC = Pattern.compile("^\\[Zenith Party] (\\w+) unlocked the (\\w+) tree!$");

    // BOSS_CLEANSE_ROOM message is always sent with but always before "Spawned new Boss room" when opening the cleanse room
    // set a flag to indicate the next "Spawned new Boss room" is the cleanse room, not an actual boss room
    private static boolean bossCleanseRoomFlag = false;

    public static void onChatMessage(Text message) {
        if (!ShardTracker.inZenithShard()) return;
        String line = message.getString();
        if (parseAbility(line)) return;
        if (parseRoom(line)) return;
        if (parseBossCleanseRoom(line)) return;
        if (parseNextFloor(line)) return;
        if (parseAspect(line)) return;
        if (parseWheel(line)) return;
    }

    private static boolean parseAbility(String line) {
        Matcher matcher = ABILITY.matcher(line);
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
        Optional<Gifts> gift = (active.isPresent()) ? Optional.empty() : Gifts.toEnum(ability);

        Party party = ZenithApi.getInstance().getPartyManager();
        party.createMember(player);
        switch (verb) {
            case "now has":
            case "now have":
            case "upgraded":
            case "downgraded":
                if (passive.isPresent() && spec.isPresent() && rarity.isPresent()) {
                    party.addAbility(player, new Passive(passive.get(), spec.get(), rarity.get()));
                } else if (curse.isPresent()) {
                    party.addAbility(player, curse.get());
                } else if (active.isPresent() && spec.isPresent() && rarity.isPresent()) {
                    party.addAbility(player, new Active(active.get(), spec.get(), rarity.get()));
                } else if (gift.isPresent()) {
                    party.addGift(player, gift.get());
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

    private static boolean parseAspect(String line) {
        Matcher matcher = ASPECT.matcher(line);
        if (!matcher.matches()) return false;
        String player = matcher.group(1);
        Optional<Aspect> aspect = Aspect.toEnum(matcher.group(2));
        if (aspect.isEmpty()) return true;

        Party party = ZenithApi.getInstance().getPartyManager();
        party.createMember(player);
        party.setAspect(player, aspect.get());
        return true;
    }

    private static boolean parseRoom(String line) {
        Matcher matcher = ROOM.matcher(line);
        if (!matcher.matches()) return false;
        Optional<Rooms> roomOptional = Rooms.toEnum(matcher.group(1));
        if (roomOptional.isEmpty()) return true;
        Rooms room = roomOptional.get();
        if (room == Rooms.BOSS && bossCleanseRoomFlag) {
            room = Rooms.BOSS_CLEANSE;
        }

        ZenithApiEvents.ROOM_SPAWNED.invoker().onRoomSpawned(room, matcher.group(2) != null);
        bossCleanseRoomFlag = false;
        return true;
    }

    private static boolean parseBossCleanseRoom(String line) {
        Matcher matcher = BOSS_CLEANSE_ROOM.matcher(line);
        if (!matcher.matches()) return false;
        bossCleanseRoomFlag = true;
        return true;
    }

    private static boolean parseNextFloor(String line) {
        Matcher matcher = NEXT_FLOOR.matcher(line);
        if (!matcher.matches()) return false;
        ZenithApiEvents.SENT_TO_NEXT_FLOOR.invoker().onSentToNextFloor();
        return true;
    }

    private static boolean parseWheel(String line) {
        Matcher matcher1 = PURGING_STONE_WHEEL.matcher(line);
        Party party = ZenithApi.getInstance().getPartyManager();
        if (matcher1.matches()) {
            String player = matcher1.group(1);
            party.downgradeAll(player);
            return true;
        }
        Matcher matcher2 = WHEEL_UPGRADE_2.matcher(line);
        if (matcher2.matches()) {
            String player = matcher2.group(1);
            party.upgradeBy2(player);
            return true;
        }
        Matcher matcher3 = WHEEL_SPEC.matcher(line);
        if (matcher3.matches()) {
            String player = matcher3.group(1);
            Optional<Spec> spec = Spec.toEnum(matcher3.group(2));
            if (spec.isEmpty()) return true;
            party.addSpec(player, spec.get());
            return true;
        }
        return false;
    }
}
