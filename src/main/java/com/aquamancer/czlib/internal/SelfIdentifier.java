package com.aquamancer.czlib.internal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SelfIdentifier {
    private static final String SELF_TITLE = "Current Abilities";
    private static boolean listening = false;

    private static int selfHeadSlot;
    private static String selfName = "";

    public static void onOpenScreenPacket(OpenScreenS2CPacket packet) {
        if (!ShardTracker.inZenithShard()) return;
        if (packet.getName().getString().equals(SELF_TITLE)) {
            listening = true;
        }
    }

    public static void onInventoryPacketParsed(String name, int headSlot) {
        if (listening) {
            selfHeadSlot = headSlot;
            selfName = name;
        }
        listening = false;
    }

    public static int getSelfHeadSlot() {
        return selfHeadSlot;
    }

    public static String getSelfName() {
        if (selfName == null || selfName.isBlank()) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.player == null) return "";
            selfName = client.player.getName().getString();
        }
        return selfName;
    }

    public static boolean isSelf(String name) {
        if (name == null) return false;
        return name.equals(selfName);
    }
}
