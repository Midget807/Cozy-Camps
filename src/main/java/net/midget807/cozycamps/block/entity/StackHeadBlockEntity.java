package net.midget807.cozycamps.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.util.math.BlockPos;

public class StackHeadBlockEntity extends SkullBlockEntity {
    public StackHeadBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
    }
}
