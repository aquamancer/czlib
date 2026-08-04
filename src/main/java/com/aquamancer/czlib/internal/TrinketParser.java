package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.api.Party;
import com.aquamancer.czlib.api.ZenithApi;
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

import static com.aquamancer.czlib.internal.TooltipParser.parseSpecRarity;

@ApiStatus.Internal
public class TrinketParser {
    private static final int TRINKET_SIZE = 6*9;
    private static final int EXPECTED_INV_SIZE = TRINKET_SIZE + 4*9;

    private static final List<Integer> SPEC_SLOTS = List.of(2, 3, 5, 6);
    private static final int HEAD_SLOT = 4;

    private static final int ASPECT_SLOT = 9;

    private static final Set<Integer> activeSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 17);

    private static final int PASSIVES_START = 27;
    private static final int PASSIVES_END = 44;
    private static final List<Integer> PLAYER_HEAD_SLOTS = List.of(47, 48, 50, 51);

    private static final Item PLAYER_HEAD = Registries.ITEM.get(new Identifier("minecraft", "player_head"));
    private static final Item CURRENTLY_SELECTED_ITEM = Registries.ITEM.get(new Identifier("minecraft", "green_stained_glass_pane"));
    private static final Item EMPTY_SLOT = Registries.ITEM.get(new Identifier("minecraft", "light_gray_stained_glass_pane"));
    private static final Item NO_ACTIVE = Registries.ITEM.get(new Identifier("minecraft", "red_stained_glass_pane"));

    private static final Pattern GRAVE_TIMER = Pattern.compile("Grave Timer:\\s+(\\d+\\.\\d+)s");

    public static void onInventoryS2CPacket(InventoryS2CPacket packet, MinecraftClient client) {
        if (client.player != null && packet.getSyncId() != 0) {
//            List<ItemStack> inv = packet.getContents();
//            client.player.sendMessage(Text.literal("Inventory packet, syncId="+packet.getSyncId()+",size=" + inv.size() + ": " + inv.stream().map((stack) -> {return stack.getName().getString();}).toList().toString()));
//            client.player.sendMessage(Text.literal("inventory packet syncid: " + packet.getSyncId() + ", size: " + packet.getContents().size()));
//            client.player.sendMessage(Text.literal("inventory packet syncid: " + packet.getSyncId() + ", revision: " + packet.getRevision() + "\nsize: " + inv.size()));
        }
        List<ItemStack> inv = packet.getContents();
        if (inv.size() < EXPECTED_INV_SIZE) return;  // player's inventory
        if (!isDepthsTrinket(inv)) return;

        HeadParseResult headParseResult = parsePlayerHeads(inv);
        UpdateManager.getInstance().setHeadNames(headParseResult.names);
        if (!headParseResult.success) return;

        Party party = ZenithApi.getInstance().getPartyManager();
        party.setMembers(headParseResult.names.keySet());
        party.setGraveTimers(headParseResult.graveTimers);

        String player = headParseResult.currentlySelected.orElse(null);
        if (player == null) return;  // also guarantees Party.players contains the current player after setMembers()
        ZenithApi.getInstance().setCurrentlySelected(player);
        if (!UpdateManager.getInstance().shouldParseTrinketPacket(player)) return;
        SelfIdentifier.onInventoryPacketParsed(player, headParseResult.names.get(player));
//        client.player.sendMessage(Text.literal("Inventory packet received for " + player));

        PassiveParseResult passiveParseResult = parsePassives(inv);
        party.setPassives(player, passiveParseResult.passives, passiveParseResult.curses);

        boolean hasPride = passiveParseResult.curses.contains(Curse.PRIDE);
        EnumSet<Spec> specs = parseSpecs(inv, hasPride);
        party.setSpecs(player, specs);

        Optional<Aspect> aspect = parseAspect(inv);
        party.setAspect(player, aspect.orElse(null));

        Set<Active> actives = parseActives(inv);
        party.setActives(player, actives);

        if (client.player != null && packet.getSyncId() != 0) {
////            client.player.sendMessage(Text.literal("inventory packet syncid: " + packet.getSyncId() + ", size: " + packet.getContents().size()));
//            client.player.sendMessage(Text.literal("inventory packet syncid: " + packet.getSyncId() + ", revision: " + packet.getRevision() + "\nsize: " + inv.size() + "\n" + inv.stream().map((stack) -> {
////                return stack.getName().getString();
////                return stack.toHoverableText().getString();
//                return stack.getTooltip(null, TooltipContext.BASIC).stream().map(text -> text.getString()).toList().toString();
////                return stack.getTooltip(null, TooltipContext.BASIC).stream().map(text -> text.toString()).toList();
//            }).toList().toString()));
        }
    }

    private static boolean isDepthsTrinket(List<ItemStack> inv) {
        return parseHeadName(inv.get(HEAD_SLOT).getName().getString()).isPresent();
    }

    private record HeadParseResult(List<Integer> validHeads, Map<String, Integer> names, Map<String, Optional<Double>> graveTimers, Optional<String> currentlySelected, boolean success) {}
    private static HeadParseResult parsePlayerHeads(List<ItemStack> inv) {
        List<Integer> validHeads = new ArrayList<>(PLAYER_HEAD_SLOTS.size());
        Map<String, Integer> names = new HashMap<>(PLAYER_HEAD_SLOTS.size());
        Map<String, Optional<Double>> graveTimers = new HashMap<>(PLAYER_HEAD_SLOTS.size());
        String currentlySelected = null;

        for (Integer slot : PLAYER_HEAD_SLOTS) {
            if (inv.get(slot).getItem() != PLAYER_HEAD && inv.get(slot).getItem() != CURRENTLY_SELECTED_ITEM) continue;
            List<Text> tooltip = inv.get(slot).getTooltip(null, TooltipContext.BASIC);
            if (tooltip.size() < 2) continue;
            Optional<String> name = parseHeadName(tooltip.get(0).getString());
            if (name.isEmpty()) continue;
            Optional<Double> graveTimer = Optional.empty();
            String line2 = tooltip.get(1).getString();
            if (line2.equals("Currently Shown")) {
                currentlySelected = name.get();
                if (tooltip.size() >= 3) {
                    graveTimer = parseHeadGraveTimer(tooltip.get(2).getString());
                }
            } else {
                graveTimer = parseHeadGraveTimer(line2);
            }

            validHeads.add(slot);
            names.put(name.get(), slot);
            graveTimers.put(name.get(), graveTimer);
        }

        return new HeadParseResult(
                validHeads,
                names,
                graveTimers,
                Optional.ofNullable(currentlySelected),
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

    private record PassiveParseResult(EnumMap<Passives, Passive> passives, EnumSet<Curse> curses) {}
    private static PassiveParseResult parsePassives(List<ItemStack> inv) {
        EnumMap<Passives, Passive> passives = new EnumMap<>(Passives.class);
        EnumSet<Curse> curses = EnumSet.noneOf(Curse.class);
        for (int slot = PASSIVES_START; slot <= PASSIVES_END; slot++) {
            ItemStack item = inv.get(slot);
            if (item.getItem() == EMPTY_SLOT || item.isEmpty()) break;
            String line1 = item.getName().getString();

            Optional<Passives> passiveName = Passives.fromString(line1);
            if (passiveName.isPresent()) {
                List<Text> tooltip = item.getTooltip(null, TooltipContext.BASIC);
                if (tooltip.size() < 2) continue;
                String line2 = tooltip.get(1).getString();
                TooltipParser.SpecRarityParseResult specAndRarity = TooltipParser.parseSpecRarity(line2);
                if (specAndRarity.spec().isEmpty() || specAndRarity.rarity().isEmpty()) continue;
                passives.put(passiveName.get(), new Passive(passiveName.get(), specAndRarity.spec().get(), specAndRarity.rarity().get()));
                continue;
            }

            Optional<Curse> curse = Curse.fromString(line1);
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
            Optional<Spec> spec = Spec.fromString(line1);
            if (spec.isPresent()) {
                specs.add(spec.get());
            }
        }
        return specs;
    }

    private static Optional<Aspect> parseAspect(List<ItemStack> inv) {
        return Aspect.fromString(inv.get(ASPECT_SLOT).getName().getString());
    }

    // multiple wildcards via convergence is handled by not having convergence replace previous wildcards
    // parsed via gui/chat
    private static Set<Active> parseActives(List<ItemStack> inv) {
        Set<Active> actives = new HashSet<>();
        for (Integer slot : activeSlots) {
            if (inv.get(slot).getItem() == NO_ACTIVE) continue;
            ItemStack item = inv.get(slot);
            Optional<Actives> ability = Actives.fromString(item.getName().getString());
            if (ability.isEmpty()) continue;
            List<Text> tooltip = item.getTooltip(null, TooltipContext.BASIC);
            if (tooltip.size() < 2) continue;
            String line2 = tooltip.get(1).getString();
            TooltipParser.SpecRarityParseResult specAndRarity = parseSpecRarity(line2);
            if (specAndRarity.spec().isEmpty() || specAndRarity.rarity().isEmpty()) continue;

            actives.add(new Active(ability.get(), specAndRarity.spec().get(), specAndRarity.rarity().get()));
        }
        return actives;
    }
}
