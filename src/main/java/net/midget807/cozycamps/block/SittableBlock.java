package net.midget807.cozycamps.block;

import com.mojang.serialization.MapCodec;
import net.midget807.cozycamps.entity.SitEntity;
import net.midget807.cozycamps.registry.ModEntities;
import net.midget807.cozycamps.registry.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.function.BiConsumer;

public class SittableBlock extends Block {
    public static final MapCodec<SittableBlock> CODEC = createCodec(SittableBlock::new);
    private double yOffset = 0;

    public SittableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient() && player.getMainHandStack().isEmpty() && !player.isSneaking()) {
            Entity entity = null;
            List<SitEntity> entities = world.getEntitiesByType(ModEntities.SIT, new Box(pos), sitEntity -> true);
            if (entities.isEmpty()) {
                updateYOffest(state);
                entity = ModEntities.SIT.spawn((ServerWorld) world, pos, SpawnReason.TRIGGERED);
                entity.setPosition(entity.getPos().add(0, yOffset, 0));
            } else {
                entity = entities.get(0);
            }
            player.startRiding(entity, true);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        killSitEntities(world, pos);
        return super.onBreak(world, pos, state, player);
    }

    private void killSitEntities(World world, BlockPos pos) {
        if (!world.isClient) {
            List<SitEntity> entities = world.getEntitiesByType(ModEntities.SIT, new Box(pos), sitEntity -> true);
            for (SitEntity entity : entities) {
                entity.discard();
            }
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        super.onDestroyedByExplosion(world, pos, explosion);
        killSitEntities(world, pos);
    }

    @Override
    protected void onExploded(BlockState state, World world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        killSitEntities(world, pos);
        super.onExploded(state, world, pos, explosion, stackMerger);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public void setYOffsetVoxel(double maxY) {
        this.yOffset = maxY / 16.0;
    }
    public void updateYOffest(BlockState state) {
    }
}
