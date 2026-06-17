package net.midget807.cozycamps.mixin;

import net.midget807.cozycamps.block.StakeType;
import net.midget807.cozycamps.registry.ModBlocks;
import net.midget807.cozycamps.registry.ModProperties;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void cozycamps$ignoreSkullItemIfIsPointedStake(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (context.getWorld().getBlockState(context.getBlockPos()).isOf(ModBlocks.STAKE)) {
            if (context.getWorld().getBlockState(context.getBlockPos()).get(ModProperties.STAKE_PART) == StakeType.Part.POINT) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void cozycamps$ignoreSkullItemIfIsPointedStake(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (context.getWorld().getBlockState(context.getBlockPos()).isOf(ModBlocks.STAKE)) {
            if (context.getWorld().getBlockState(context.getBlockPos()).get(ModProperties.STAKE_PART) == StakeType.Part.POINT) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }

}
