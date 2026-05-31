package net.midget807.cozycamps.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.midget807.cozycamps.CozyCampsMain;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public static final TagKey<Item> STUMP_SHAPER = TagKey.of(RegistryKeys.ITEM, CozyCampsMain.id("stump_shaper"));

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        this.getOrCreateTagBuilder(STUMP_SHAPER)
                .add(
                        Items.WOODEN_AXE,
                        Items.STONE_AXE,
                        Items.IRON_AXE,
                        Items.GOLDEN_AXE,
                        Items.DIAMOND_AXE,
                        Items.NETHERITE_AXE
                );
    }
}
