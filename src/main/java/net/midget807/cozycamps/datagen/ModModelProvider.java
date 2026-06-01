package net.midget807.cozycamps.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.block.StumpType;
import net.midget807.cozycamps.registry.ModBlocks;
import net.midget807.cozycamps.registry.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        this.registerStump(ModBlocks.OAK_STUMP, "oak", blockStateModelGenerator);
        this.registerStump(ModBlocks.SPRUCE_STUMP, "spruce", blockStateModelGenerator);
        this.registerStump(ModBlocks.BIRCH_STUMP, "birch", blockStateModelGenerator);
        this.registerStump(ModBlocks.JUNGLE_STUMP, "jungle", blockStateModelGenerator);
        this.registerStump(ModBlocks.ACACIA_STUMP, "acacia", blockStateModelGenerator);
        this.registerStump(ModBlocks.CHERRY_STUMP, "cherry", blockStateModelGenerator);
        this.registerStump(ModBlocks.DARK_OAK_STUMP, "dark_oak", blockStateModelGenerator);
        this.registerStump(ModBlocks.MANGROVE_STUMP, "mangrove", blockStateModelGenerator);
    }

    private void registerStump(Block block, String woodType, BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(block)))
                        .coordinate(BlockStateVariantMap.create(ModProperties.STUMP_SIZE, ModProperties.STUMP_HEIGHT)
                                .register(StumpType.Size.LARGE, StumpType.Height.TALL, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/large_tall_" + woodType + "_stump")))
                                .register(StumpType.Size.LARGE, StumpType.Height.SHORT, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/large_short_" + woodType + "_stump")))
                                .register(StumpType.Size.MEDIUM, StumpType.Height.TALL, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/medium_tall_" + woodType + "_stump")))
                                .register(StumpType.Size.MEDIUM, StumpType.Height.SHORT, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/medium_short_" + woodType + "_stump")))
                                .register(StumpType.Size.SMALL, StumpType.Height.TALL, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/small_tall_" + woodType + "_stump")))
                                .register(StumpType.Size.SMALL, StumpType.Height.SHORT, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/small_short_" + woodType + "_stump")))
                        )
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
