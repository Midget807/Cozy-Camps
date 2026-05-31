package net.midget807.cozycamps;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.midget807.cozycamps.datagen.ModBlockTagProvider;
import net.midget807.cozycamps.datagen.ModItemTagProvider;
import net.midget807.cozycamps.datagen.ModLangProvider;
import net.midget807.cozycamps.datagen.ModLootTableProvider;
import net.midget807.cozycamps.datagen.ModModelProvider;
import net.midget807.cozycamps.datagen.ModRecipeProvider;

public class CozyCampsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModLangProvider::new);
		pack.addProvider(ModRecipeProvider::new);
	}
}
