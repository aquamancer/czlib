package com.aquamancer.czlib.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ZenithApiUpdateEvents {
    public static Event<ActiveUpdate> ACTIVE = EventFactory.createArrayBacked(
            ActiveUpdate.class,
            (listeners) -> (player) -> {
                for (ActiveUpdate listener : listeners) {
                    listener.onActiveUpdate(player);
                }
            }
    );

    public static Event<PassiveUpdate> PASSIVE = EventFactory.createArrayBacked(
            PassiveUpdate.class,
            (listeners) -> (player) -> {
                for (PassiveUpdate listener : listeners) {
                    listener.onPassiveUpdate(player);
                }
            }
    );

    public static Event<CurseUpdate> CURSE = EventFactory.createArrayBacked(
            CurseUpdate.class,
            (listeners) -> (player) -> {
                for (CurseUpdate listener : listeners) {
                    listener.onCurseUpdate(player);
                }
            }
    );

    public static Event<GraveTimerUpdate> GRAVE_TIMER = EventFactory.createArrayBacked(
            GraveTimerUpdate.class,
            (listeners) -> (player) -> {
                for (GraveTimerUpdate listener : listeners) {
                    listener.onGraveTimerUpdate(player);
                }
            }
    );

    public static Event<SpecUpdate> SPEC = EventFactory.createArrayBacked(
            SpecUpdate.class,
            (listeners) -> (player) -> {
                for (SpecUpdate listener : listeners) {
                    listener.onSpecUpdate(player);
                }
            }
    );

    public static Event<AspectUpdate> ASPECT = EventFactory.createArrayBacked(
            AspectUpdate.class,
            (listeners) -> (player) -> {
                for (AspectUpdate listener : listeners) {
                    listener.onAspectUpdate(player);
                }
            }
    );

    public static Event<GiftUpdate> GIFT = EventFactory.createArrayBacked(
            GiftUpdate.class,
            (listeners) -> (player) -> {
                for (GiftUpdate listener : listeners) {
                    listener.onGiftUpdate(player);
                }
            }
    );

    public static Event<VzcUpdate> VZC = EventFactory.createArrayBacked(
            VzcUpdate.class,
            (listeners) -> (player) -> {
                for (VzcUpdate listener : listeners) {
                    listener.onVzcUpdate(player);
                }
            }
    );


    @FunctionalInterface
    public interface ActiveUpdate {
        void onActiveUpdate(String player);
    }

    @FunctionalInterface
    public interface PassiveUpdate {
        void onPassiveUpdate(String player);
    }

    @FunctionalInterface
    public interface CurseUpdate {
        void onCurseUpdate(String player);
    }

    @FunctionalInterface
    public interface GraveTimerUpdate {
        void onGraveTimerUpdate(String player);
    }

    @FunctionalInterface
    public interface SpecUpdate {
        void onSpecUpdate(String player);
    }

    @FunctionalInterface
    public interface AspectUpdate {
        void onAspectUpdate(String player);
    }

    @FunctionalInterface
    public interface GiftUpdate {
        void onGiftUpdate(String player);
    }

    @FunctionalInterface
    public interface VzcUpdate {
        void onVzcUpdate(String player);
    }
}
