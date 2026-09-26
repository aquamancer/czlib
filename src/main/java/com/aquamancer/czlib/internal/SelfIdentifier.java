package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.internal.event.ZenithApiInternalEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

/**
 * Identifies the name of the client player by checking who is "Currently Selected" in the first received inventory packet following any trinket open screen packet
 * Note that clicking on party members' player heads in the trinket do not result in open screen packets; only inventory packets
 */
@ApiStatus.Internal
public class SelfIdentifier {
    private static final String TRINKET_TITLE = "Current Abilities";
    private static boolean listening = false;
    private static int listeningId = -1;

    private static int selfHeadSlot;
    private static String selfName = "";

    public static void init() {
        ZenithApiInternalEvents.WORLD_CHANGED.register(() -> {
            listeningId = -1;
        });
    }

    public static void onOpenScreenPacket(OpenScreenS2CPacket packet) {
        if (!ShardTracker.inZenithShard()) return;
        if (packet.getSyncId() == listeningId) return;  // may already have been consumed
        if (packet.getName().getString().equals(TRINKET_TITLE)) {
            listening = true;
            listeningId = packet.getSyncId();
        }
    }

    public static void onInventoryPacketParsed(String name, int headSlot, int syncId) {
        if (listening && syncId == listeningId) {
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
