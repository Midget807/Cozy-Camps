package net.midget807.cozycamps.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.block.LogBenchType;
import net.midget807.cozycamps.block.StumpType;
import net.midget807.cozycamps.registry.ModBlocks;
import net.midget807.cozycamps.registry.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(ModBlocks.SACK);

        this.registerStump(ModBlocks.OAK_STUMP, "oak", blockStateModelGenerator);
        this.registerStump(ModBlocks.SPRUCE_STUMP, "spruce", blockStateModelGenerator);
        this.registerStump(ModBlocks.BIRCH_STUMP, "birch", blockStateModelGenerator);
        this.registerStump(ModBlocks.JUNGLE_STUMP, "jungle", blockStateModelGenerator);
        this.registerStump(ModBlocks.ACACIA_STUMP, "acacia", blockStateModelGenerator);
        this.registerStump(ModBlocks.CHERRY_STUMP, "cherry", blockStateModelGenerator);
        this.registerStump(ModBlocks.DARK_OAK_STUMP, "dark_oak", blockStateModelGenerator);
        this.registerStump(ModBlocks.MANGROVE_STUMP, "mangrove", blockStateModelGenerator);

        this.registerLogBench(ModBlocks.OAK_LOG_BENCH, "oak", blockStateModelGenerator);
        this.registerLogBench(ModBlocks.SPRUCE_LOG_BENCH, "spruce", blockStateModelGenerator);
        this.registerLogBench(ModBlocks.BIRCH_LOG_BENCH, "birch", blockStateModelGenerator);
        this.registerLogBench(ModBlocks.JUNGLE_LOG_BENCH, "jungle", blockStateModelGenerator);
        this.registerLogBench(ModBlocks.ACACIA_LOG_BENCH, "acacia", blockStateModelGenerator);
        this.registerLogBench(ModBlocks.CHERRY_LOG_BENCH, "cherry", blockStateModelGenerator);
        this.registerLogBench(ModBlocks.DARK_OAK_LOG_BENCH, "dark_oak", blockStateModelGenerator);
        this.registerLogBench(ModBlocks.MANGROVE_LOG_BENCH, "mangrove", blockStateModelGenerator);
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

    private void registerLogBench(Block block, String woodType, BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(block)))
                        .coordinate(BlockStateVariantMap.create(ModProperties.LOG_BENCH_OFFSET, HorizontalFacingBlock.FACING)
                                .register(LogBenchType.Offset.CENTER, Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/" + woodType + "_log_bench")))
                                .register(LogBenchType.Offset.CENTER, Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/" + woodType + "_log_bench")).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                .register(LogBenchType.Offset.CENTER, Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/" + woodType + "_log_bench")).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(LogBenchType.Offset.CENTER, Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/" + woodType + "_log_bench")).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                                .register(LogBenchType.Offset.EDGE, Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/" + woodType + "_log_bench_edge")))
                                .register(LogBenchType.Offset.EDGE, Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/" + woodType + "_log_bench_edge")).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                .register(LogBenchType.Offset.EDGE, Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/" + woodType + "_log_bench_edge")).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(LogBenchType.Offset.EDGE, Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, CozyCampsMain.id("block/" + woodType + "_log_bench_edge")).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                        )
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
