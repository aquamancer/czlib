package com.aquamancer.czlib;

import com.aquamancer.czlib.api.ZenithApi;
import com.aquamancer.czlib.internal.*;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

public class Czlib implements ClientModInitializer {
	public static final String MOD_ID = "czlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		WorldChangeTracker.init();
		UpdateManager.init();
		ShardTracker.init();
		ScreenCanceler.init();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
