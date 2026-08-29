package com.aquamancer.czlib.api.event;

import com.aquamancer.czlib.api.bosses.Boss;
import com.aquamancer.czlib.api.rooms.Room;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ZenithApiStateEvents {
    public static final Event<RoomEvent> ROOM_SPAWNED = EventFactory.createArrayBacked(
            RoomEvent.class,
            (listeners) -> {
                return (room, wildcard) -> {
                    for (RoomEvent listener : listeners) {
                        listener.onRoomEvent(room, wildcard);
                    }
                };
            }
    );
    public static final Event<RoomEvent> ROOM_REWARD = EventFactory.createArrayBacked(
            RoomEvent.class,
            (listeners) -> {
                return (room, wildcard) -> {
                    for (RoomEvent listener : listeners) {
                        listener.onRoomEvent(room, wildcard);
                    }
                };
            }
    );
    public static final Event<BossEvent> BOSS_SPAWNED = EventFactory.createArrayBacked(
            BossEvent.class,
            (listeners) -> {
                return (boss) -> {
                    for (BossEvent listener : listeners) {
                        listener.onBossEvent(boss);
                    }
                };
            }
    );
    public static final Event<FloorEvent> FLOOR_CLEARED = EventFactory.createArrayBacked(
            FloorEvent.class,
            (listeners) -> {
                return (floor) -> {
                    for (FloorEvent listener : listeners) {
                        listener.onFloorEvent(floor);
                    }
                };
            }
    );
    public static final Event<FloorEvent> SENT_TO_NEXT_FLOOR = EventFactory.createArrayBacked(
            FloorEvent.class,
            (listeners) -> {
                return (floor) -> {
                    for (FloorEvent listener : listeners) {
                        listener.onFloorEvent(floor);
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
    public static final Event<GraveSpawned> GRAVE_SPAWNED = EventFactory.createArrayBacked(
            GraveSpawned.class,
            (listeners) -> {
                return (deadPlayer) -> {
                    for (GraveSpawned listener : listeners) {
                        listener.onGraveSpawn(deadPlayer);
                    }
                };
            }
    );
    public static final Event<SentToLootroom> SENT_TO_LOOTROOM = EventFactory.createArrayBacked(
            SentToLootroom.class,
            (listeners) -> {
                return () -> {
                    for (SentToLootroom listener : listeners) {
                        listener.onSentToLootroom();
                    }
                };
            }
    );

    @FunctionalInterface
    public interface RoomEvent {
        void onRoomEvent(Room room, boolean isWildcard);
    }
    @FunctionalInterface
    public interface FloorEvent {
        void onFloorEvent(int floor);
    }
    @FunctionalInterface
    public interface BossEvent {
        void onBossEvent(Boss boss);
    }
    @FunctionalInterface
    public interface EnterZenithShard {
        void onEnteredZenithShard(String previous, String current);
    }
    @FunctionalInterface
    public interface ExitZenithShard {
        void onExitZenithShard(String previous, String current);
    }
    @FunctionalInterface
    public interface GraveSpawned {
        void onGraveSpawn(String deadPlayer);
    }
    @FunctionalInterface
    public interface SentToLootroom {
        void onSentToLootroom();
    }
}
