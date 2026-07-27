package com.aquamancer.czlib.api.event;

import com.aquamancer.czlib.api.PartyMember;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Set;

public class ZenithApiUpdateEvents {

    public static final Event<PartyUpdate> PARTY_MEMBER = EventFactory.createArrayBacked(
            PartyUpdate.class,
            listeners -> names -> {
                for (PartyUpdate listener : listeners) {
                    listener.onPartyUpdate(names);
                }
            }
    );

    public static final Event<PartyMemberUpdate> ASPECT = createPartyMemberEvent();
    public static final Event<PartyMemberUpdate> ACTIVE = createPartyMemberEvent();
    public static final Event<PartyMemberUpdate> PASSIVE = createPartyMemberEvent();
    public static final Event<PartyMemberUpdate> CURSE = createPartyMemberEvent();
    public static final Event<PartyMemberUpdate> GIFT = createPartyMemberEvent();
    public static final Event<PartyMemberUpdate> GRAVE_TIMER = createPartyMemberEvent();
    public static final Event<PartyMemberUpdate> SPEC = createPartyMemberEvent();
    public static final Event<PartyMemberUpdate> VZC = createPartyMemberEvent();

    private static Event<PartyMemberUpdate> createPartyMemberEvent() {
        return EventFactory.createArrayBacked(
                PartyMemberUpdate.class,
                listeners -> player -> {
                    for (PartyMemberUpdate listener : listeners) {
                        listener.onUpdate(player);
                    }
                }
        );
    }

    @FunctionalInterface
    public interface PartyUpdate {
        void onPartyUpdate(Set<String> names);
    }

    @FunctionalInterface
    public interface PartyMemberUpdate {
        void onUpdate(PartyMember player);
    }
}
