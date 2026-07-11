package com.aquamancer.czlib.internal.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ZenithApiInternalEvents {

    public static final Event<WorldChanged> WORLD_CHANGED = EventFactory.createArrayBacked(
            WorldChanged.class,
            (listeners) -> {
                return () -> {
                    for (WorldChanged listener : listeners) {
                        listener.onWorldChanged();
                    }
                };
            }
    );

    @FunctionalInterface
    public interface WorldChanged {
        void onWorldChanged();
    }

}
