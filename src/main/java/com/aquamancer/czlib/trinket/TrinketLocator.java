package com.aquamancer.czlib.trinket;

import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class TrinketLocator {
    private static InventoryS2CPacket lastInventoryPacket;
    private static int lastTrinketSlot;


}
