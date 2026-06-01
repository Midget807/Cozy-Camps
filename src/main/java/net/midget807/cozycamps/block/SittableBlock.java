package net.midget807.cozycamps.block;

import com.mojang.serialization.MapCodec;
import net.midget807.cozycamps.entity.SitEntity;
import net.midget807.cozycamps.registry.ModEntities;
import net.midget807.cozycamps.registry.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.function.BiConsumer;

/**Classes that extend {@link SittableBlock} must override the relevant {@link SittableBlock#updateXOffset(BlockState)}, {@link SittableBlock#updateYOffset(BlockState)} and {@link SittableBlock#updateZOffset(BlockState)}.<br>
 * <br>
 * This is so the {@link SitEntity} is offset to the correct height based on the block's {@link VoxelShape}.
 * The {@link VoxelShape} should be retrieved from {@link AbstractBlock#getOutlineShape(BlockState, BlockView, BlockPos, ShapeContext)}.<br>
 * Note that the default sit position (offset of [0, 0, 0]) is the center of a block at its bottom face (a default offset of [0.5, 0, 0.5]). <br>
 * <br>
 * An all-encompassing method {@link SittableBlock#updateSitOffset(BlockState)} and its variants can manipulate multiple axes simultaneously.*/
public class SittableBlock extends Block {
    public static final MapCodec<SittableBlock> CODEC = createCodec(SittableBlock::new);
    private double xOffset = 0;
    private double yOffset = 0;
    private double zOffset = 0;

    public SittableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient() && player.getMainHandStack().isEmpty() && !player.isSneaking()) {
            Entity entity = null;
            List<SitEntity> entities = world.getEntitiesByType(ModEntities.SIT, new Box(pos), sitEntity -> true);
            if (entities.isEmpty()) {
                updateSitOffset(state);
                entity = ModEntities.SIT.spawn((ServerWorld) world, pos, SpawnReason.TRIGGERED);
                entity.setPosition(entity.getPos().add(xOffset, yOffset, zOffset));
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

    public double getXOffset() {
        return xOffset;
    }
    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }
    public void setXOffsetVoxel(double maxX) {
        this.xOffset = maxX / 16.0;
    }
    /**See {@link StumpBlock#updateYOffset(BlockState)} for an exmaple override.*/
    public void updateXOffset(BlockState state) {
    }

    public double getYOffset() {
        return yOffset;
    }
    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }
    public void setYOffsetVoxel(double maxY) {
        this.yOffset = maxY / 16.0;
    }
    /**See {@link StumpBlock#updateYOffset(BlockState)} for an exmaple override.*/
    public void updateYOffset(BlockState state) {
    }

    public double getZOffset() {
        return zOffset;
    }
    public void setZOffset(double zOffset) {
        this.zOffset = zOffset;
    }
    public void setZOffsetVoxel(double maxZ) {
        this.zOffset = maxZ / 16.0;
    }
    /**See {@link StumpBlock#updateYOffset(BlockState)} for an exmaple override.*/
    public void updateZOffset(BlockState state) {
    }

    public void setSitOffset(double xOffset, double yOffset, double zOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }
    public void setSitOffsetVoxel(double maxX, double maxY, double maxZ) {
        this.xOffset = maxX / 16.0;
        this.yOffset = maxY / 16.0;
        this.zOffset = maxZ / 16.0;
    }

    /**Override the relevant X, Y, Z update methods: <br>
     * - {@link #updateXOffset(BlockState)} <br>
     * - {@link #updateYOffset(BlockState)} <br>
     * - {@link #updateZOffset(BlockState)} <br>
     * See also: <br>
     * - {@link #setSitOffset(double, double, double)} <br>
     * - {@link #setSitOffsetVoxel(double, double, double)}*/
    public void updateSitOffset(BlockState state) {
        updateXOffset(state);
        updateYOffset(state);
        updateZOffset(state);
    }


}
