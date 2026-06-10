package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.block.entity.SackBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {
    public static final BlockEntityType<SackBlockEntity> SACK = register("sack",
            BlockEntityType.Builder.<SackBlockEntity>create(SackBlockEntity::new, ModBlocks.SACK)
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, CozyCampsMain.id(name), builder.build());
    }

    public static void registerModBlockEntities() {
        CozyCampsMain.loggerRegistry("Block Entities");
    }
}
