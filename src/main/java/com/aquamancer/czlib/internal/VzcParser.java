package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.Czlib;
import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.api.abils.AbilitySpec;
import com.aquamancer.czlib.api.abils.Spec;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VzcParser {
    private static final Pattern TITLE = Pattern.compile("(\\w+)'s Charms$");
    private static final int EXPECTED_INV_SIZE = 90;
    private static final int CHARM_SUMMARY_SLOT = 9;
    private static final Item BOOK = Registries.ITEM.get(new Identifier("minecraft", "book"));

    private static String listening = null;
    private static int listeningId = -1;

    public static void onOpenScreenPacket(OpenScreenS2CPacket packet) {
        String title = packet.getName().getString();
        Matcher matcher = TITLE.matcher(title);
        if (!matcher.matches()) return;
        listening = matcher.group(1);
        listeningId = packet.getSyncId();
    }

    public static void onInventoryPacket(InventoryS2CPacket packet) {
        if (listening == null) return;
        if (packet.getSyncId() != listeningId) return;
        if (!ZenithApi.getInstance().isPartyMember(listening)) return;

        List<ItemStack> inv = packet.getContents();
        if (inv.size() < EXPECTED_INV_SIZE) return;
        ItemStack book = inv.get(CHARM_SUMMARY_SLOT);
        if (book.getItem() != BOOK) return;
        if (!book.getName().getString().equals("Charm Effect Summary")) return;
        List<String> tooltip = book.getTooltip(null, TooltipContext.BASIC).stream().map(Text::getString).toList();
        if (tooltip.size() < 4) return;

        EnumMap<Spec, Integer> lineCounts = new EnumMap<>(Spec.class);
        int i = 1;
        if (tooltip.get(1).equals("These Charms are currently disabled!")) {
            i = 2;
        }
        for (; i < tooltip.size() - 3; i++) {
            Optional<AbilitySpec> spec = parseSpec(tooltip.get(i));
            if (spec.isEmpty()) continue;
            Optional<Spec> converted = spec.get().toSpec();
            if (converted.isEmpty()) continue;
            lineCounts.compute(converted.get(), (k, v) -> {
                return (v == null) ? 1 : v + 1;
            });
        }

        ZenithApi.getInstance().getPartyManager().setCharmLines(listening, lineCounts);
        listening = null;
    }

    private static Optional<AbilitySpec> parseSpec(String line) {
        Optional<AbilitySpec> result = Optional.empty();

        int left = 0;
        int right = line.indexOf(' ');
        int stop = line.indexOf(" :");
        if (stop == -1) return result;
        StringBuilder name = new StringBuilder();
        while (right != -1 && right <= stop) {
            name.append(line, left, right);
            result = AbilitySpec.fromAbilityName(name.toString());
            if (result.isPresent()) {
                break;
            } else {
                left = right;
                right = line.indexOf(' ', left + 1);
            }
        }
        return result;
    }
}