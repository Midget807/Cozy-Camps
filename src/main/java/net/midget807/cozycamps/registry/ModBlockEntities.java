package net.midget807.cozycamps.registry;

import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.block.entity.SackBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class ModBlockEntities {
    public static final BlockEntityType<SackBlockEntity> SACK = register("sack",
            BlockEntityType.Builder.<SackBlockEntity>create(SackBlockEntity::new, ModBlocks.SACK)
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, CozyCampsMain.id(name), builder.build());
    }

    public static void registerModBlockEntities() {
        CozyCampsMain.loggerRegistry("Block Entities");
        addCustomSupportedBlocksForVanillaBE();
    }

    private static void addCustomSupportedBlocksForVanillaBE() {
        Set<Block> skullBEBlocks = ((BlockEntityTypeAccessor) BlockEntityType.SKULL).getBlocks();
        skullBEBlocks.add(ModBlocks.STAKE);
    }
}
