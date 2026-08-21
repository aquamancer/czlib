package com.aquamancer.czlib.internal;

import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.api.event.ZenithApiStateEvents;
import com.aquamancer.czlib.api.rooms.Rooms;
import com.aquamancer.czlib.internal.event.ZenithApiInternalEvents;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.stream.Stream;

@ApiStatus.Internal
public class UpdateManager {
    private static UpdateManager INSTANCE;

    private boolean enabled = false;
    private Map<String, Integer> headNames = new HashMap<>(4);
    private int lastScreenSyncId = 0;

    private static final int CHAT_UPDATE_DELAY_TICKS = 4*20;
    private static final int MIN_TICKS_BETWEEN_FULL_UPDATE = 8*20;
    private static final int MIN_TICKS_BETWEEN_PARSE = 1;
    private static final int CLOSE_VZC_DELAY_TICKS = 2;

    private int ticksUntilUpdate = CHAT_UPDATE_DELAY_TICKS;
    private int ticksSinceFullUpdate = 0;
    private final Map<String, Integer> ticksSinceParse = new HashMap<>(4);
    private int ticksUntilCloseVzc = -1;

    public static void init() {
        ZenithApiInternalEvents.WORLD_CHANGED.register(() -> getInstance().onWorldChange());
        ClientTickEvents.START_CLIENT_TICK.register((client) -> getInstance().onTick());
        ZenithApiStateEvents.ENTER_ZENITH_SHARD.register((p, c) -> {
            getInstance().enabled = true;
        });
        ZenithApiStateEvents.EXIT_ZENITH_SHARD.register((p, c) -> {
            getInstance().ticksSinceParse.clear();
            getInstance().headNames.clear();
            getInstance().enabled = false;
        });
        ZenithApiStateEvents.SENT_TO_LOOTROOM.register(() -> {
            getInstance().ticksSinceParse.clear();
            getInstance().headNames.clear();
            getInstance().enabled = false;
        });

        ZenithApiStateEvents.ROOM_SPAWNED.register((r, w) -> {
            getInstance().openVzc(getInstance().headNames.keySet());
        });
    }

    // update rules
    public void onManualScreenClose(Screen closedScreen) {
        if (!enabled) return;

        if (closedScreen == null) return;
        String title = closedScreen.getTitle().getString();
        if (title.equals("Crafting") || title.equals("Current Abilities")) return;
        this.update(SelfIdentifier.getSelfName());
    }

    public void onActionBarMessage(Text message) {
        if (!enabled) return;

        if (message.getString().equals("Ability removed!")) {
            ZenithApi.getInstance().removeAbilityForFloor();
            this.update(SelfIdentifier.getSelfName());
        }
    }

    public void onZenithChatMessage() {
        if (!enabled) return;
        if (!ShardTracker.inZenithShard()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;
        this.ticksUntilUpdate = CHAT_UPDATE_DELAY_TICKS;
    }

    public void onArmorChange(EntityEquipmentUpdateS2CPacket packet, MinecraftClient client) {
        if (!enabled) return;
        if (client.world == null) return;
        Entity entity = client.world.getEntityById(packet.getId());
        if (!(entity instanceof PlayerEntity player)) return;
        String name = player.getName().getString();
        if (!ZenithApi.getInstance().isPartyMember(name)) return;
        List<Pair<EquipmentSlot, ItemStack>> changed = packet.getEquipmentList();
        if (changed.size() <= 2) return;  // magic number from testing to filter out durability changes
//        if (changed.size() == 1 && changed.get(0).getFirst() == EquipmentSlot.MAINHAND) return;
        Stream<ItemStack> stacks = changed.stream().map(Pair::getSecond);
        if (stacks.allMatch(ItemStack::isEmpty)) return;

        openVzc(Collections.singleton(name));
    }


    public void onTick() {
        if (!enabled) return;
        if (!ShardTracker.inZenithShard()) return;

        if (ticksUntilCloseVzc == 0) {
            sendPacket(new CloseHandledScreenC2SPacket(this.lastScreenSyncId));
            ticksUntilCloseVzc--;
        } else if (ticksUntilCloseVzc > 0) {
            ticksUntilCloseVzc--;
        }

        for (Map.Entry<String, Integer> entry : ticksSinceParse.entrySet()) {
            entry.setValue(entry.getValue() + 1);
        }
        if (ticksUntilUpdate == 0) {
            this.updateAll();
            ticksUntilUpdate--;  // go to -1 to indicate idling
        } else if (ticksUntilUpdate > 0) {
            ticksUntilUpdate--;
        }
        ticksSinceFullUpdate++;
    }

    public boolean shouldParseTrinketPacket(String player) {
        Integer elapsed = ticksSinceParse.get(player);
        if (elapsed == null) {
            ticksSinceParse.put(player, 0);
            return true;
        } else {
            if (elapsed >= MIN_TICKS_BETWEEN_PARSE) {
                ticksSinceParse.put(player, 0);  // assume parser will parse
                return true;
            }
        }
        return false;
    }

    // internals

    // todo make package-private
    public static UpdateManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UpdateManager();
        }
        return INSTANCE;
    }

    public void onOpenScreenPacket(OpenScreenS2CPacket packet) {
        this.lastScreenSyncId = packet.getSyncId();
    }

    private void onWorldChange() {
        this.lastScreenSyncId = 0;
    }

    void setHeadNames(Map<String, Integer> slotMapping) {
        this.headNames = slotMapping;
    }

    private void updateAll() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;
        if (client.currentScreen instanceof HandledScreen) return;
        if (ScreenCanceler.isCancelingScreens()) return;
        if (ZenithApi.getInstance().getCurrentRoomType() == Rooms.TREE_SELECT) return;
        if (ticksSinceFullUpdate < MIN_TICKS_BETWEEN_FULL_UPDATE) {
            ticksUntilUpdate = MIN_TICKS_BETWEEN_FULL_UPDATE - ticksSinceFullUpdate;
        }

        int trinketSlot = TrinketLocator.getTrinketSlot();
        Set<Integer> slotsToClick = new HashSet<>(headNames.values());
        slotsToClick.remove(SelfIdentifier.getSelfHeadSlot());
//        client.player.sendMessage(Text.literal("Updating all players: "+slotsToClick+", starting syncid="+this.lastScreenSyncId+",current screenhandler syncid="+client.player.currentScreenHandler.syncId));
        this.lastScreenSyncId = TrinketOpener.openAndClickHeads(slotsToClick, trinketSlot, this.lastScreenSyncId);
        this.ticksUntilUpdate = -1;
        this.ticksSinceFullUpdate = 0;
    }
    // todo make private
    public void update(String player) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;
        if (client.currentScreen instanceof HandledScreen) return;
        if (ScreenCanceler.isCancelingScreens()) return;
        if (ZenithApi.getInstance().getCurrentRoomType() == Rooms.TREE_SELECT) return;

        Set<Integer> slotsToClick;
        if (SelfIdentifier.isSelf(player)) {
            slotsToClick = null;
        } else {
            Integer headSlot = headNames.get(player);
            if (headSlot == null) return;
            slotsToClick = Set.of(headSlot);
        }

        int trinketSlot = TrinketLocator.getTrinketSlot();
        if (trinketSlot == -1) {
            // todo remove this debugging
            client.player.sendMessage(Text.literal("Could not find Depths Trinket in inventory"));
            return;
        }
//        client.player.sendMessage(Text.literal("Attempting trinket update for: " + player +", clicking slots: "+slotsToClick));

        this.lastScreenSyncId = TrinketOpener.openAndClickHeads(slotsToClick, trinketSlot, this.lastScreenSyncId);
    }

    public void openVzc(Collection<String> names) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.player.networkHandler == null) return;
        if (client.currentScreen instanceof HandledScreen) return;
        if (ScreenCanceler.isCancelingScreens()) return;
        if (ZenithApi.getInstance().getCurrentRoomType() == Rooms.TREE_SELECT) return;

        ScreenCanceler.cancelFutureScreens(names.size(), ScreenCanceler.Type.VZC);
        for (String name : names) {
            client.player.networkHandler.sendChatCommand("vzc " + name);
        }
        this.lastScreenSyncId += names.size();
        // have to send closescreens2cpacket after > 1 tick delay since
        // server defers commands to main thread instead of handling in network thread
        // so sending closescreens2cpacket instantly is too early
        ticksUntilCloseVzc = CLOSE_VZC_DELAY_TICKS;
        this.ticksSinceFullUpdate = 0;
    }

    public static void sendPacket(Packet<?> packet) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() != null && client.getNetworkHandler().getConnection() != null) {
            client.getNetworkHandler().getConnection().send(packet);
        }
    }

    // todo remove
    public boolean isEnabled() {
        return enabled;
    }

    private UpdateManager() {}
}
