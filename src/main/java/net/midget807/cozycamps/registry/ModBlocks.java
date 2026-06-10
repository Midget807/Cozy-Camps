package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.block.LogBenchBlock;
import net.midget807.cozycamps.block.SackBlock;
import net.midget807.cozycamps.block.SittableBlock;
import net.midget807.cozycamps.block.StumpBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModBlocks {
    public static final Map<Identifier, Block> BLOCKS_WITH_ITEM = new LinkedHashMap<>();

    public static final Block SACK = registerBlock("sack", new SackBlock(null, createSackBlockSettings(DyeColor.YELLOW)));
    public static final Block WHITE_SACK = registerBlock("white_sack", new SackBlock(DyeColor.WHITE, createSackBlockSettings(DyeColor.WHITE)));
    public static final Block ORANGE_SACK = registerBlock("orange_sack", new SackBlock(DyeColor.ORANGE, createSackBlockSettings(DyeColor.ORANGE)));
    public static final Block MAGENTA_SACK = registerBlock("magenta_sack", new SackBlock(DyeColor.MAGENTA, createSackBlockSettings(DyeColor.MAGENTA)));
    public static final Block LIGHT_BLUE_SACK = registerBlock("light_blue_sack", new SackBlock(DyeColor.LIGHT_BLUE, createSackBlockSettings(DyeColor.LIGHT_BLUE)));
    public static final Block YELLOW_SACK = registerBlock("yellow_sack", new SackBlock(DyeColor.YELLOW, createSackBlockSettings(DyeColor.YELLOW)));
    public static final Block LIME_SACK = registerBlock("lime_sack", new SackBlock(DyeColor.LIME, createSackBlockSettings(DyeColor.LIME)));
    public static final Block PINK_SACK = registerBlock("pink_sack", new SackBlock(DyeColor.PINK, createSackBlockSettings(DyeColor.PINK)));
    public static final Block GRAY_SACK = registerBlock("gray_sack", new SackBlock(DyeColor.GRAY, createSackBlockSettings(DyeColor.GRAY)));
    public static final Block LIGHT_GRAY_SACK = registerBlock("light_gray_sack", new SackBlock(DyeColor.LIGHT_GRAY, createSackBlockSettings(DyeColor.LIGHT_GRAY)));
    public static final Block CYAN_SACK = registerBlock("cyan_sack", new SackBlock(DyeColor.CYAN, createSackBlockSettings(DyeColor.CYAN)));
    public static final Block BLUE_SACK = registerBlock("blue_sack", new SackBlock(DyeColor.BLUE, createSackBlockSettings(DyeColor.BLUE)));
    public static final Block BROWN_SACK = registerBlock("brown_sack", new SackBlock(DyeColor.BROWN, createSackBlockSettings(DyeColor.BROWN)));
    public static final Block GREEN_SACK = registerBlock("green_sack", new SackBlock(DyeColor.GREEN, createSackBlockSettings(DyeColor.GREEN)));
    public static final Block RED_SACK = registerBlock("red_sack", new SackBlock(DyeColor.RED, createSackBlockSettings(DyeColor.RED)));
    public static final Block BLACK_SACK = registerBlock("black_sack", new SackBlock(DyeColor.BLACK, createSackBlockSettings(DyeColor.BLACK)));
    public static final Block PURPLE_SACK = registerBlock("purple_sack", new SackBlock(DyeColor.PURPLE, createSackBlockSettings(DyeColor.PURPLE)));

    public static final Block OAK_STUMP = register("oak_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block SPRUCE_STUMP = register("spruce_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block BIRCH_STUMP = register("birch_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block JUNGLE_STUMP = register("jungle_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block ACACIA_STUMP = register("acacia_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block CHERRY_STUMP = register("cherry_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block DARK_OAK_STUMP = register("dark_oak_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block MANGROVE_STUMP = register("mangrove_stump", new StumpBlock(createStumpBlockSettings()));

    public static final Block OAK_LOG_BENCH = register("oak_log_bench", new LogBenchBlock(createStumpBlockSettings()));
    public static final Block SPRUCE_LOG_BENCH = register("spruce_log_bench", new LogBenchBlock(createStumpBlockSettings()));
    public static final Block BIRCH_LOG_BENCH = register("birch_log_bench", new LogBenchBlock(createStumpBlockSettings()));
    public static final Block JUNGLE_LOG_BENCH = register("jungle_log_bench", new LogBenchBlock(createStumpBlockSettings()));
    public static final Block ACACIA_LOG_BENCH = register("acacia_log_bench", new LogBenchBlock(createStumpBlockSettings()));
    public static final Block CHERRY_LOG_BENCH = register("cherry_log_bench", new LogBenchBlock(createStumpBlockSettings()));
    public static final Block DARK_OAK_LOG_BENCH = register("dark_oak_log_bench", new LogBenchBlock(createStumpBlockSettings()));
    public static final Block MANGROVE_LOG_BENCH = register("mangrove_log_bench", new LogBenchBlock(createStumpBlockSettings()));

    private static Block register(String name, Block block) {
        registerBlockItem(name, block);
        Block registered = registerBlock(name, block);
        BLOCKS_WITH_ITEM.put(CozyCampsMain.id(name), registered);
        return registered;
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, CozyCampsMain.id(name), new BlockItem(block, new Item.Settings()));
    }

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, CozyCampsMain.id(name), block);
    }

    public static AbstractBlock.Settings createStumpBlockSettings() {
        return AbstractBlock.Settings.create().nonOpaque().mapColor(MapColor.SPRUCE_BROWN).strength(1.5f).sounds(BlockSoundGroup.WOOD).burnable().nonOpaque();
    }

    public static AbstractBlock.Settings createSackBlockSettings(DyeColor color) {
        return AbstractBlock.Settings.create().nonOpaque().mapColor(color).strength(0.5F).sounds(BlockSoundGroup.WOOL).burnable();
    }

    public static void registerModBlocks() {
        CozyCampsMain.loggerRegistry("Blocks");
    }
}
