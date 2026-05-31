package net.midget807.cozycamps;

import net.fabricmc.api.ModInitializer;

import net.midget807.cozycamps.registry.ModBlocks;
import net.midget807.cozycamps.registry.ModEntities;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CozyCampsMain implements ModInitializer {
	public static final String MOD_ID = "cozycamps";
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static void loggerRegistry(String path) {
        CozyCampsMain.LOGGER.info("Registering Cozy Camps {}", path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Camping time");

		ModBlocks.registerModBlocks();
		ModEntities.registerModEntities();
	}
}