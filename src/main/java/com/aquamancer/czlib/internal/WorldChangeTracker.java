package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.internal.event.ZenithApiInternalEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;

public class WorldChangeTracker {
    static {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> onTick(client));
    }
    private static World lastWorld;

    public static void onTick(MinecraftClient client) {
        World currentWorld = client.world;
        if (lastWorld != currentWorld) {
            lastWorld = currentWorld;
            ZenithApiInternalEvents.WORLD_CHANGED.invoker().onWorldChanged();
        }
    }
}
