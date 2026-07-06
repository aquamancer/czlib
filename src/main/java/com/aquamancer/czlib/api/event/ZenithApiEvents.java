package com.aquamancer.czlib.api.event;

import com.aquamancer.czlib.api.ZenithApi;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ZenithApiEvents {
    @FunctionalInterface
    public interface HardUpdate {
        void onHardUpdate(ZenithApi api);

        Event<HardUpdate> EVENT = EventFactory.createArrayBacked(
                HardUpdate.class,
                listeners -> {
                    return (api -> {
                        for (HardUpdate listener : listeners) {
                            listener.onHardUpdate(api);
                        }
                    });
                }
        );
    }
}
