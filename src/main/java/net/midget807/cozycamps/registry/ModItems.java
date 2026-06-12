package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.item.SackItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    public static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();

    public static final Item SACK = register("sack", new SackItem(ModBlocks.SACK, new Item.Settings().maxCount(1)));
    public static final Item WHITE_SACK = register("white_sack", new SackItem(ModBlocks.WHITE_SACK, new Item.Settings().maxCount(1)));
    public static final Item ORANGE_SACK = register("orange_sack", new SackItem(ModBlocks.ORANGE_SACK, new Item.Settings().maxCount(1)));
    public static final Item MAGENTA_SACK = register("magenta_sack", new SackItem(ModBlocks.MAGENTA_SACK, new Item.Settings().maxCount(1)));
    public static final Item LIGHT_BLUE_SACK = register("light_blue_sack", new SackItem(ModBlocks.LIGHT_BLUE_SACK, new Item.Settings().maxCount(1)));
    public static final Item YELLOW_SACK = register("yellow_sack", new SackItem(ModBlocks.YELLOW_SACK, new Item.Settings().maxCount(1)));
    public static final Item LIME_SACK = register("lime_sack", new SackItem(ModBlocks.LIME_SACK, new Item.Settings().maxCount(1)));
    public static final Item PINK_SACK = register("pink_sack", new SackItem(ModBlocks.PINK_SACK, new Item.Settings().maxCount(1)));
    public static final Item GRAY_SACK = register("gray_sack", new SackItem(ModBlocks.GRAY_SACK, new Item.Settings().maxCount(1)));
    public static final Item LIGHT_GRAY_SACK = register("light_gray_sack", new SackItem(ModBlocks.LIGHT_GRAY_SACK, new Item.Settings().maxCount(1)));
    public static final Item CYAN_SACK = register("cyan_sack", new SackItem(ModBlocks.CYAN_SACK, new Item.Settings().maxCount(1)));
    public static final Item BLUE_SACK = register("blue_sack", new SackItem(ModBlocks.BLUE_SACK, new Item.Settings().maxCount(1)));
    public static final Item BROWN_SACK = register("brown_sack", new SackItem(ModBlocks.BROWN_SACK, new Item.Settings().maxCount(1)));
    public static final Item GREEN_SACK = register("green_sack", new SackItem(ModBlocks.GREEN_SACK, new Item.Settings().maxCount(1)));
    public static final Item RED_SACK = register("red_sack", new SackItem(ModBlocks.RED_SACK, new Item.Settings().maxCount(1)));
    public static final Item BLACK_SACK = register("black_sack", new SackItem(ModBlocks.BLACK_SACK, new Item.Settings().maxCount(1)));
    public static final Item PURPLE_SACK = register("purple_sack", new SackItem(ModBlocks.PURPLE_SACK, new Item.Settings().maxCount(1)));

    private static Item register(String name, Item item) {
        ITEMS.put(CozyCampsMain.id(name), item);
        return Registry.register(Registries.ITEM, CozyCampsMain.id(name), item);
    }

    public static void registerModItems() {
        CozyCampsMain.loggerRegistry("Items");
    }
}
