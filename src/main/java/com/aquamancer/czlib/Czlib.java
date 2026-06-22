package com.aquamancer.czlib;

import com.aquamancer.czlib.trinket.TrinketOpener;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(
					ClientCommandManager.literal("sid")
							.then(ClientCommandManager.argument("syncId", IntegerArgumentType.integer(0))
									.executes(context -> {
										TrinketOpener.syncId = IntegerArgumentType.getInteger(context, "syncId");
										return 1;
									})
							)
			);
			dispatcher.register(
					ClientCommandManager.literal("auto")
							.then(ClientCommandManager.argument("auto", IntegerArgumentType.integer(0))
									.executes(context -> {
										int t = IntegerArgumentType.getInteger(context, "auto");
                                        TrinketOpener.autoClick = t != 0;
										return 1;
									})
							)
			);
			dispatcher.register(
					ClientCommandManager.literal("delay")
							.then(ClientCommandManager.argument("delay", IntegerArgumentType.integer(0))
									.executes(context -> {
										TrinketOpener.delayMillis = IntegerArgumentType.getInteger(context, "delay");
										return 1;
									})
							)
			);
		});

		ClientTickEvents.START_CLIENT_TICK.register((client) -> {
			TrinketOpener.onTick(client);
		});
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
