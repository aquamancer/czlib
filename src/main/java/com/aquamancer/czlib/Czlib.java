package com.aquamancer.czlib;

import com.aquamancer.czlib.internal.ScreenCanceler;
import com.aquamancer.czlib.internal.ShardTracker;
import com.aquamancer.czlib.internal.UpdateManager;
import com.aquamancer.czlib.internal.WorldChangeTracker;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
