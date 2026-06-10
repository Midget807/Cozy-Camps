package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.item.SackItem;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    public static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();

    public static final Item SACK = register("sack", new SackItem(ModBlocks.SACK, new Item.Settings().maxCount(1)));

    private static Item register(String name, Item item) {
        ITEMS.put(CozyCampsMain.id(name), item);
        return Registry.register(Registries.ITEM, CozyCampsMain.id(name), item);
    }

    public static void registerModItems() {
        CozyCampsMain.loggerRegistry("Items");
    }
}
