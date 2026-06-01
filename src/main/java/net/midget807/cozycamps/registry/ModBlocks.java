package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.block.SittableBlock;
import net.midget807.cozycamps.block.StumpBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ModBlocks {
    public static final Map<Identifier, Block> BLOCKS_WITH_ITEM = new HashMap<Identifier, Block>();

    public static final Block OAK_STUMP = register("oak_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block SPRUCE_STUMP = register("spruce_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block BIRCH_STUMP = register("birch_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block JUNGLE_STUMP = register("jungle_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block ACACIA_STUMP = register("acacia_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block CHERRY_STUMP = register("cherry_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block DARK_OAK_STUMP = register("dark_oak_stump", new StumpBlock(createStumpBlockSettings()));
    public static final Block MANGROVE_STUMP = register("mangrove_stump", new StumpBlock(createStumpBlockSettings()));

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
        return AbstractBlock.Settings.create().mapColor(MapColor.SPRUCE_BROWN).strength(1.5f).sounds(BlockSoundGroup.WOOD).burnable().nonOpaque();
    }

    public static void registerModBlocks() {
        CozyCampsMain.loggerRegistry("Blocks");
    }
}
