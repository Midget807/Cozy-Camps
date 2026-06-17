package net.midget807.cozycamps.mixin;

import com.mojang.datafixers.types.Type;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.midget807.cozycamps.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SkullBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;


@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin<T extends BlockEntity> {
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void cozycamps$addSupportedBlock(BlockEntityType.BlockEntityFactory<? extends T> factory, Set<Block> blocks, Type<?> type, CallbackInfo ci) {
        BlockEntityType<?> self = (BlockEntityType<?>) (Object) this;

        if (self == BlockEntityType.SKULL) {
            //noinspection UnstableApiUsage
            ((BlockEntityTypeAccessor) self).getBlocks().add(ModBlocks.STAKE);
        }
    }
}
