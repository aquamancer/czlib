package com.aquamancer.czlib.trinket;

import com.aquamancer.czlib.api.abils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApiStatus.Internal
public class TrinketParser {
    private static final int TRINKET_SIZE = 6*9;
    private static final int EXPECTED_INV_SIZE = TRINKET_SIZE + 4*9;

    private static final List<Integer> SPEC_SLOTS = List.of(2, 3, 5, 6);
    private static final int HEAD_SLOT = 4;

    private static final List<? extends Class<?>> ABILITY_SLOTS = List.of(
            Aspect.class
    );
    private static final int ASPECT_SLOT = 9;
    private static final int RIGHT

    private static final int PASSIVES_START = 27;
    private static final int PASSIVES_END = 44;
    private static final List<Integer> PLAYER_HEAD_SLOTS = List.of(47, 48, 50, 51);

    private static final String SPEC_RARITY_SPLIT = " : ";

    private static final Item PLAYER_HEAD = Registries.ITEM.get(new Identifier("minecraft", "player_head"));
    private static final Item CURRENTLY_SELECTED_ITEM = Registries.ITEM.get(new Identifier("minecraft", "green_stained_glass_pane"));
    private static final Item EMPTY_SLOT = Registries.ITEM.get(new Identifier("minecraft", "light_gray_stained_glass_pane"));

    private static final Pattern GRAVE_TIMER = Pattern.compile("Grave Timer:\\s+(\\d+\\.\\d+)s");

    public static void onInventoryS2CPacket(InventoryS2CPacket packet, MinecraftClient client) {
        if (packet.getSyncId() == 0) return;  // player's inventory
        List<ItemStack> inv = packet.getContents();
        if (inv.size() != EXPECTED_INV_SIZE) return;
        if (!isDepthsTrinket(inv)) return;
        HeadParseResult headParseResult = parsePlayerHeads(inv);
        UpdateManager.getInstance().setHeadsToClick(headParseResult.validHeads);
        if (!headParseResult.success) return;
        String player = headParseResult.names.get(headParseResult.currentlySelected);
        if (player == null) return;
        PassiveParseResult passiveParseResult = parsePassives(inv);
        boolean hasPride = passiveParseResult.curses.contains(Curse.PRIDE);
        EnumSet<Spec> specs = parseSpecs(inv, hasPride);
        


//        parsePlayerHeads(inv, tooltipCache);

        if (client.player != null && packet.getSyncId() != 0) {
//            client.player.sendMessage(Text.literal("inventory packet syncid: " + packet.getSyncId() + ", size: " + packet.getContents().size()));
            client.player.sendMessage(Text.literal("inventory packet syncid: " + packet.getSyncId() + ", revision: " + packet.getRevision() + "\nsize: " + inv.size() + "\n" + inv.stream().map((stack) -> {
//                return stack.getName().getString();
//                return stack.toHoverableText().getString();
                return stack.getTooltip(null, TooltipContext.BASIC).stream().map(text -> text.getString()).toList().toString();
//                return stack.getTooltip(null, TooltipContext.BASIC).stream().map(text -> text.toString()).toList();
            }).toList().toString()));
        }
    }

    private static boolean isDepthsTrinket(List<ItemStack> inv) {
        return parseHeadName(inv.get(HEAD_SLOT).getName().getString()).isPresent();
    }

    private record HeadParseResult(List<Integer> validHeads, Map<Integer, String> names, Map<Integer, Optional<Double>> graveTimers, int currentlySelected, boolean success) {}
    private static HeadParseResult parsePlayerHeads(List<ItemStack> inv) {
        List<Integer> validHeads = new ArrayList<>(PLAYER_HEAD_SLOTS.size());
        Map<Integer, String> names = new HashMap<>(PLAYER_HEAD_SLOTS.size());
        Map<Integer, Optional<Double>> graveTimers = new HashMap<>(PLAYER_HEAD_SLOTS.size());
        int currentlySelected = -1;

        for (Integer slot : PLAYER_HEAD_SLOTS) {
            if (inv.get(slot).getItem() != PLAYER_HEAD && inv.get(slot).getItem() != CURRENTLY_SELECTED_ITEM) continue;
            List<Text> tooltip = inv.get(slot).getTooltip(null, TooltipContext.BASIC);
            if (tooltip.size() < 2) continue;
            Optional<String> name = parseHeadName(tooltip.get(0).getString());
            if (name.isEmpty()) continue;
            Optional<Double> graveTimer = Optional.empty();
            String line2 = tooltip.get(1).getString();
            if (line2.equalsIgnoreCase("Currently Shown")) {
                currentlySelected = slot;
                if (tooltip.size() >= 3) {
                    graveTimer = parseHeadGraveTimer(tooltip.get(2).getString());
                }
            } else {
                graveTimer = parseHeadGraveTimer(line2);
            }

            validHeads.add(slot);
            names.put(slot, name.get());
            graveTimers.put(slot, graveTimer);
        }

        return new HeadParseResult(
                validHeads,
                names,
                graveTimers,
                currentlySelected,
                !validHeads.isEmpty()
        );
    }

    private static Optional<String> parseHeadName(String line) {
        int split = line.indexOf("'s Abilities");
        return (split > 0) ? Optional.of(line.substring(0, split)) : Optional.empty();
    }

    private static Optional<Double> parseHeadGraveTimer(String line) {
        Matcher matcher = GRAVE_TIMER.matcher(line);
        if (matcher.find()) {
            return Optional.of(Double.parseDouble(matcher.group(1)));
        }
        return Optional.empty();
    }

    private record PassiveParseResult(List<Passive> passives, EnumSet<Curse> curses) {}
    private static PassiveParseResult parsePassives(List<ItemStack> inv) {
        List<Passive> passives = new ArrayList<>(PASSIVES_END - PASSIVES_START + 1);
        EnumSet<Curse> curses = EnumSet.noneOf(Curse.class);
        for (int slot = PASSIVES_START; slot <= PASSIVES_END; slot++) {
            ItemStack item = inv.get(slot);
            if (item.getItem() == EMPTY_SLOT || item.isEmpty()) break;
            String line1 = item.getName().getString();

            Optional<Passives> passiveName = Passives.toEnum(line1);
            if (passiveName.isPresent()) {
                List<Text> tooltip = item.getTooltip(null, TooltipContext.BASIC);
                if (tooltip.size() < 2) continue;
                String line2 = tooltip.get(1).getString();
                int split = line2.indexOf(SPEC_RARITY_SPLIT);
                if (split <= 0) continue;
                int rarityStart = split + SPEC_RARITY_SPLIT.length();
                if (rarityStart == line2.length()) continue;  // no chars after the split
                Optional<AbilitySpec> spec = AbilitySpec.toEnum(line2.substring(0, split));
                if (spec.isEmpty()) continue;
                Optional<Rarity> rarity = Rarity.toEnum(line2.substring(rarityStart));
                if (rarity.isEmpty()) continue;
                passives.add(new Passive(passiveName.get(), spec.get(), rarity.get()));
                continue;
            }
            Optional<Curse> curse = Curse.toEnum(line1);
            if (curse.isPresent()) {
                curses.add(curse.get());
                continue;
            }
        }
        return new PassiveParseResult(passives, curses);
    }

    private static EnumSet<Spec> parseSpecs(List<ItemStack> inv, boolean hasPride) {
        if (hasPride) {
            return EnumSet.allOf(Spec.class);
        }

        EnumSet<Spec> specs = EnumSet.noneOf(Spec.class);
        for (Integer slot : SPEC_SLOTS) {
            String line1 = inv.get(slot).getName().getString();
            Optional<Spec> spec = Spec.toEnum(line1);
            if (spec.isPresent()) {
                specs.add(spec.get());
            }
        }
        return specs;
    }

    private static Optional<Aspect> parseAspect(List<ItemStack> inv) {
        return Aspect.toEnum(inv.get(ASPECT_SLOT).getName());
    }

    private static Enum<?> parseAbilities(List<ItemStack> inv) {
        for (Class<?> type : ABILITY_SLOTS) {

        }
    }
}
