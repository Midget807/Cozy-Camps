package net.midget807.cozycamps.mixin;

import net.midget807.cozycamps.registry.ModBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
    @Inject(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getChoiceType(Lcom/mojang/datafixers/DSL$TypeReference;Ljava/lang/String;)Lcom/mojang/datafixers/types/Type;"))
    private static <T extends BlockEntity> void cozycamps$addBlock(String id, BlockEntityType.Builder<T> builder, CallbackInfoReturnable<BlockEntityType<T>> cir) {
        if (id.equals("skull")) {
            builder.blocks.add(ModBlocks.STAKE);
        }
    }
}
