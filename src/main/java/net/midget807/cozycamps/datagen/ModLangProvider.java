package net.midget807.cozycamps.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.midget807.cozycamps.block.SackBlock;
import net.midget807.cozycamps.registry.ModBlocks;
import net.midget807.cozycamps.registry.ModItemGroups;
import net.midget807.cozycamps.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModLangProvider extends FabricLanguageProvider {
    public ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        this.addItemGroup(translationBuilder, ModItemGroups.MAIN, "Cozy Camps");
        ModBlocks.BLOCKS_WITH_ITEM.forEach((identifier, block) -> addFromIdName(translationBuilder, block));
        ModItems.ITEMS.forEach((identifier, item) -> addFromIdName(translationBuilder, item));
        translationBuilder.add("container.cozycamps.sack", "Sack");
    }

    private void addItemGroup(TranslationBuilder translationBuilder, ItemGroup itemGroup, String name) {
        translationBuilder.add(itemGroup.getDisplayName().getString(), name);
    }

    private void addFromIdName(TranslationBuilder translationBuilder, Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        String path = id.getPath();
        String[] elements = path.split("_");
        StringBuilder name = new StringBuilder();
        for (String element : elements) {
            name.append(element.substring(0, 1).toUpperCase().concat(element.substring(1)).concat(" "));
        }
        translationBuilder.add(block.getTranslationKey(), name.toString().trim());
    }
    private void addFromIdName(TranslationBuilder translationBuilder, Item item) {
        Identifier id = Registries.ITEM.getId(item);
        String path = id.getPath();
        String[] elements = path.split("_");
        StringBuilder name = new StringBuilder();
        for (String element : elements) {
            name.append(element.substring(0, 1).toUpperCase().concat(element.substring(1)).concat(" "));
        }
        translationBuilder.add(item.getTranslationKey(), name.toString().trim());
    }
}
