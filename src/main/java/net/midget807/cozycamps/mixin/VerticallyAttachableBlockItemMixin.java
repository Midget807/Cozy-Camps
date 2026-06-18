package net.midget807.cozycamps.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VerticallyAttachableBlockItem.class)
public abstract class VerticallyAttachableBlockItemMixin extends BlockItem {
    public VerticallyAttachableBlockItemMixin(Block block, Settings settings) {
        super(block, settings);
    }

    @Inject(method = "getPlacementState", at = @At("HEAD"), cancellable = true)
    private void cozycamps$nullPlacementStateForSkullOnStake(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir) {
        if (this.getDefaultStack().isIn(ItemTags.SKULLS)) {
            cir.setReturnValue(null);
        }
    }
}
