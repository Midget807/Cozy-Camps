package net.midget807.cozycamps;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.fabricmc.fabric.mixin.object.builder.BlockEntityTypeMixin;
import net.midget807.cozycamps.registry.*;
import net.minecraft.block.entity.BlockEntityType;
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

		ModDataComponents.registerModDataComponents();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModItems.registerModItems();
		ModItemGroups.registerModItemGroups();
		ModEntities.registerModEntities();
		ModScreenHandlers.registerModScreenHandlers();
		ModPackets.registerModPackets();
		ModPackets.registerAllC2S();
		ModPackets.registerS2CPayloads();
		ModStats.registerModStats();

		System.out.println("SKULL blocks = " + ((BlockEntityTypeAccessor) BlockEntityType.SKULL).getBlocks());
	}
}