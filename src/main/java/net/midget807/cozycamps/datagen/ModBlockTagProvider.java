package net.midget807.cozycamps.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public static final TagKey<Block> SNEAK_INTERACTABLE = TagKey.of(RegistryKeys.BLOCK, CozyCampsMain.id("sneak_interactable"));
    public static final TagKey<Block> SITTABLE = TagKey.of(RegistryKeys.BLOCK, CozyCampsMain.id("sittable"));
    public static final TagKey<Block> STUMPS = TagKey.of(RegistryKeys.BLOCK, CozyCampsMain.id("stumps"));
    public static final TagKey<Block> LOG_BENCHES = TagKey.of(RegistryKeys.BLOCK, CozyCampsMain.id("log_benches"));
    public static final TagKey<Block> BONFIRES = TagKey.of(RegistryKeys.BLOCK, CozyCampsMain.id("bonfires"));
    public static final TagKey<Block> TENTS = TagKey.of(RegistryKeys.BLOCK, CozyCampsMain.id("tents"));
    public static final TagKey<Block> CRUCIFIXES = TagKey.of(RegistryKeys.BLOCK, CozyCampsMain.id("crucifixes"));

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        this.getOrCreateTagBuilder(STUMPS)
                .add(
                        ModBlocks.OAK_STUMP,
                        ModBlocks.SPRUCE_STUMP,
                        ModBlocks.BIRCH_STUMP,
                        ModBlocks.JUNGLE_STUMP,
                        ModBlocks.ACACIA_STUMP,
                        ModBlocks.CHERRY_STUMP,
                        ModBlocks.DARK_OAK_STUMP,
                        ModBlocks.MANGROVE_STUMP
                );
        this.getOrCreateTagBuilder(LOG_BENCHES)
                .add(
                        ModBlocks.OAK_LOG_BENCH,
                        ModBlocks.SPRUCE_LOG_BENCH,
                        ModBlocks.BIRCH_LOG_BENCH,
                        ModBlocks.JUNGLE_LOG_BENCH,
                        ModBlocks.ACACIA_LOG_BENCH,
                        ModBlocks.CHERRY_LOG_BENCH,
                        ModBlocks.DARK_OAK_LOG_BENCH,
                        ModBlocks.MANGROVE_LOG_BENCH
                );
        this.getOrCreateTagBuilder(BONFIRES);
        this.getOrCreateTagBuilder(TENTS);
        this.getOrCreateTagBuilder(CRUCIFIXES);
        this.getOrCreateTagBuilder(SITTABLE)
                .addTag(STUMPS)
                .addTag(LOG_BENCHES);
        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .addTag(STUMPS)
                .addTag(LOG_BENCHES)
                .addTag(BONFIRES)
                .addTag(CRUCIFIXES);
        this.getOrCreateTagBuilder(SNEAK_INTERACTABLE)
                .addTag(BONFIRES)
                .addTag(CRUCIFIXES)
                .addTag(SITTABLE);
    }
}
