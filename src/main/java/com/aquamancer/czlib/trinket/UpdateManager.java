package com.aquamancer.czlib.trinket;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@ApiStatus.Internal
public class UpdateManager {
    private static UpdateManager INSTANCE;

    private static final List<Integer> DEFAULT_HEAD_SLOTS = List.of(47, 48, 50, 51);
    private Set<Integer> headSlotsToClick = new HashSet<>(DEFAULT_HEAD_SLOTS);
    private int selfHeadSlot = 0;

    private UpdateManager() {}

    static UpdateManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UpdateManager();
        }
        return INSTANCE;
    }

    void setHeadsToClick(Collection<Integer> slots) {
        headSlotsToClick = new HashSet<>(slots);
    }
}
