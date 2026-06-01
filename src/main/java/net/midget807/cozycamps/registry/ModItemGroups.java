package net.midget807.cozycamps.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.midget807.cozycamps.CozyCampsMain;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroups {
    public static final ItemGroup MAIN = Registry.register(Registries.ITEM_GROUP, CozyCampsMain.id("cozycamps_main"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.cozycamps.main"))
                    .icon(() -> new ItemStack(ModBlocks.OAK_STUMP))
                    .entries((displayContext, entries) -> {
                        ModBlocks.BLOCKS_WITH_ITEM.forEach((identifier, block) -> {
                            entries.add(block);
                        });
                    })
                    .build()
    );

    public static void registerModItemGroups() {
        CozyCampsMain.loggerRegistry("Item Groups");
    }
}
