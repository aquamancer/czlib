package com.aquamancer.czlib.internal.event;

import com.aquamancer.czlib.api.rooms.Rooms;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ZenithApiInternalEvents {
    public static final Event<RoomSpawned> ROOM_SPAWNED = EventFactory.createArrayBacked(
            RoomSpawned.class,
            (listeners) -> {
                return (room, wildcard) -> {
                    for (RoomSpawned listener : listeners) {
                        listener.onRoomSpawned(room, wildcard);
                    }
                };
            }
    );

    public static final Event<EnterZenithShard> ENTER_ZENITH_SHARD = EventFactory.createArrayBacked(
            EnterZenithShard.class,
            (listeners) -> {
                return (previous, current) -> {
                    for (EnterZenithShard listener : listeners) {
                        listener.onEnteredZenithShard(previous, current);
                    }
                };
            }
    );

    public static final Event<ExitZenithShard> EXIT_ZENITH_SHARD = EventFactory.createArrayBacked(
            ExitZenithShard.class,
            (listeners) -> {
                return (previous, current) -> {
                    for (ExitZenithShard listener : listeners) {
                        listener.onExitZenithShard(previous, current);
                    }
                };
            }
    );

    @FunctionalInterface
    public interface RoomSpawned {
        void onRoomSpawned(Rooms room, boolean wildcard);
    }

    @FunctionalInterface
    public interface EnterZenithShard {
        void onEnteredZenithShard(String previous, String current);
    }

    @FunctionalInterface
    public interface ExitZenithShard {
        void onExitZenithShard(String previous, String current);
    }
}
