package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.api.Party;
import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.api.abils.*;
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

    private static final int ASPECT_SLOT = 9;

    private static final int WILDCARD_SLOT = 14;
    private static final int SWAP_SLOT = 16;
    private static final Set<Integer> activeSlots = Set.of(10, 11, 12, 13, 14, 15, 16, 17);

    private static final int PASSIVES_START = 27;
    private static final int PASSIVES_END = 44;
    private static final List<Integer> PLAYER_HEAD_SLOTS = List.of(47, 48, 50, 51);

    private static final Item PLAYER_HEAD = Registries.ITEM.get(new Identifier("minecraft", "player_head"));
    private static final Item CURRENTLY_SELECTED_ITEM = Registries.ITEM.get(new Identifier("minecraft", "green_stained_glass_pane"));
    private static final Item EMPTY_SLOT = Registries.ITEM.get(new Identifier("minecraft", "light_gray_stained_glass_pane"));
    private static final Item NO_ACTIVE = Registries.ITEM.get(new Identifier("minecraft", "red_stained_glass_pane"));

    private static final Pattern GRAVE_TIMER = Pattern.compile("Grave Timer:\\s+(\\d+\\.\\d+)s");

    public static void onInventoryS2CPacket(InventoryS2CPacket packet) {
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
        SelfIdentifier.onInventoryPacketParsed(player, headParseResult.names.get(player), packet.getSyncId());

        if (!UpdateManager.getInstance().shouldParseTrinketPacket(player)) return;
        boolean hasConvergence = Actives.fromString(inv.get(WILDCARD_SLOT).getName().getString()).orElse(null) == Actives.CONVERGENCE;

        PassiveParseResult passiveParseResult = parsePassives(inv, hasConvergence);
        party.setPassives(player, passiveParseResult.passives, passiveParseResult.curses);

        EnumSet<Spec> specs = parseSpecs(inv);
        party.setSpecs(player, specs);

        Optional<Aspect> aspect = parseAspect(inv);
        party.setAspect(player, aspect.orElse(null));

        Set<Active> actives = parseActives(inv);
        actives.addAll(passiveParseResult.convergenceWildcards);
        party.setActives(player, actives);
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

    private record PassiveParseResult(Set<Passive> passives, EnumSet<Curse> curses, List<Active> convergenceWildcards) {}
    private static PassiveParseResult parsePassives(List<ItemStack> inv, boolean parseConvergence) {
        Set<Passive> passives = new HashSet<>();
        EnumSet<Curse> curses = EnumSet.noneOf(Curse.class);
        List<Active> convergenceWildcards = new ArrayList<>();

        for (int slot = PASSIVES_START; slot <= PASSIVES_END; slot++) {
            ItemStack item = inv.get(slot);
            if (item.getItem() == EMPTY_SLOT || item.isEmpty()) break;
            String line1 = item.getName().getString();

            Optional<Passive> passive = Passive.fromItem(item);
            if (passive.isPresent()) {
                passives.add(passive.get());
                continue;
            }

            Optional<Curse> curse = Curse.fromString(line1);
            if (curse.isPresent()) {
                curses.add(curse.get());
                continue;
            }

            // wildcards stored by convergence are displayed under passives
            if (parseConvergence) {
                Optional<Active> active = Active.fromItem(item);
                if (active.isPresent()) {
                    convergenceWildcards.add(active.get());
                    continue;
                }
            }
        }

        if (Curse.fromString(inv.get(SWAP_SLOT).getName().getString()).orElse(null) == Curse.ANCHORING) {
            curses.add(Curse.ANCHORING);
        }
        return new PassiveParseResult(passives, curses, convergenceWildcards);
    }

    private static EnumSet<Spec> parseSpecs(List<ItemStack> inv) {
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

    private static Set<Active> parseActives(List<ItemStack> inv) {
        Set<Active> actives = new HashSet<>();
        for (Integer slot : activeSlots) {
            if (inv.get(slot).getItem() == NO_ACTIVE) continue;
            ItemStack item = inv.get(slot);
            Optional<Active> ability = Active.fromItem(item);
            ability.ifPresent(actives::add);
        }
        return actives;
    }
}