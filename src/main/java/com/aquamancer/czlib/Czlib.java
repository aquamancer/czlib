package com.aquamancer.czlib;

import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.internal.*;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;

@ApiStatus.Internal
public class Czlib implements ClientModInitializer {
	public static final String MOD_ID = "czlib";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// register events
		WorldChangeTracker.init();
		UpdateManager.init();
		ShardTracker.init();
		ScreenCanceler.init();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(
					ClientCommandManager.literal("trinket")
							.executes(context -> {
								UpdateManager.getInstance().update(SelfIdentifier.getSelfName());
								return 1;
							})
			);
			dispatcher.register(
					ClientCommandManager.literal("getShard")
								.executes(context -> {
									MinecraftClient client = MinecraftClient.getInstance();
									if (client == null || client.player == null) return 0;
									client.player.sendMessage(Text.literal(ShardTracker.getCurrentShard()));
									return 1;
								})
			);
			dispatcher.register(
					ClientCommandManager.literal("testTrinket")
							.then(ClientCommandManager.argument("syncId", IntegerArgumentType.integer(0))
									.executes(context -> {
//										int syncId = IntegerArgumentType.getInteger(context, "syncId");
										MinecraftClient.getInstance().player.sendMessage(Text.literal("Opening trinket with syncId: " + MinecraftClient.getInstance().player.currentScreenHandler.syncId));
										TrinketOpener.openAndClickHeads(Set.of(47, 48, 50, 53), TrinketLocator.getTrinketSlot(), MinecraftClient.getInstance().player.currentScreenHandler.syncId);
										return 1;
									})
							)
			);
			dispatcher.register(
					ClientCommandManager.literal("getTrinket")
							.executes(context -> {
//								UpdateManager.getInstance().update();
								MinecraftClient.getInstance().player.sendMessage(Text.literal(ZenithApi.getInstance().getPartyManager().toString()));
								return 1;
							})
			);
			dispatcher.register(
					ClientCommandManager.literal("getTrinketSlot")
							.executes(context -> {
								MinecraftClient.getInstance().player.sendMessage(Text.literal("Trinket slot=" + TrinketLocator.getTrinketSlot()));
								return 1;
							})
			);
			dispatcher.register(
					ClientCommandManager.literal("getSelf")
							.executes(context -> {
								MinecraftClient.getInstance().player.sendMessage(Text.literal("Self name=" + SelfIdentifier.getSelfName() + ", slot=" + SelfIdentifier.getSelfHeadSlot()));
								return 1;
							})
			);
			dispatcher.register(
					ClientCommandManager.literal("vzcall")
							.then(ClientCommandManager.argument("args", StringArgumentType.greedyString())
							.executes(context -> {
								String raw = StringArgumentType.getString(context, "args");
								UpdateManager.getInstance().openVzc(Arrays.stream(raw.split("\\s+")).toList());
								return 1;
							}))
			);
			dispatcher.register(
					ClientCommandManager.literal("isUpdating")
							.executes(context -> {
								MinecraftClient.getInstance().player.sendMessage(Text.literal("enabled="+UpdateManager.getInstance().isEnabled()));
								return 1;
							})
			);
			dispatcher.register(
					ClientCommandManager.literal("clearTrinket")
							.executes(context -> {
								ZenithApi.getInstance().getPartyManager().setMembers(Set.of());
								return 1;
							})
			);
		});
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
