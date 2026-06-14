package net.midget807.cozycamps.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.midget807.cozycamps.datagen.ModItemTagProvider;
import net.midget807.cozycamps.registry.ModProperties;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LogBenchBlock extends SittableBlock implements Waterloggable {
    public static final MapCodec<LogBenchBlock> CODEC = createCodec(LogBenchBlock::new);
    public static final VoxelShape CENTER_NS = Block.createCuboidShape(0.0D, 0.0D, 5.0D, 16.0D, 6.0D, 11.0D);
    public static final VoxelShape CENTER_WE = Block.createCuboidShape(5.0D, 0.0D, 0.0D, 11.0D, 6.0D, 16.0D);
    public static final VoxelShape NORTH = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 6.0D);
    public static final VoxelShape SOUTH = Block.createCuboidShape(0.0D, 0.0D, 10.0D, 16.0D, 6.0D, 16.0D);
    public static final VoxelShape WEST = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 6.0D, 6.0D, 16.0D);
    public static final VoxelShape EAST = Block.createCuboidShape(10.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    public static final EnumProperty<LogBenchType.Offset> TYPE = ModProperties.LOG_BENCH_OFFSET;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final ImmutableMap<BlockState, VoxelShape> shapes;

    public LogBenchBlock(Settings settings) {
        super(settings);
        this.shapes = getStateToShape(this.getStateManager());
        this.setDefaultState(this.getDefaultState().with(TYPE, LogBenchType.Offset.CENTER).with(FACING, Direction.NORTH).with(WATERLOGGED, false));
        this.setYOffsetVoxel(6.0f);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shapes.get(state);
    }
    private static ImmutableMap<BlockState, VoxelShape> getStateToShape(StateManager<Block, BlockState> stateManager) {
        Map<BlockState, VoxelShape> map = stateManager.getStates().stream()
                .collect(Collectors.toMap(Function.identity(), LogBenchBlock::getStateForShape));
        return ImmutableMap.copyOf(map);
    }

    private static VoxelShape getStateForShape(BlockState state) {
        Direction facing = (Direction) state.get(FACING);
        LogBenchType.Offset type = (LogBenchType.Offset) state.get(TYPE);
        if (type == LogBenchType.Offset.CENTER) {
            return switch (facing) {
                case NORTH, SOUTH -> CENTER_NS;
                case WEST, EAST -> CENTER_WE;
                default -> CENTER_NS;
            };
        } else {
            return switch (facing) {
                case NORTH -> NORTH;
                case SOUTH -> SOUTH;
                case EAST -> EAST;
                case WEST -> WEST;
                default -> NORTH;
            };
        }
    }


    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean isWaterSource = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
        BlockState blockState = this.getDefaultState().with(WATERLOGGED, isWaterSource);
        blockState = blockState.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
        return blockState;
    }

    @Override
    public void updateYOffset(BlockState state) {
        this.setYOffsetVoxel(6.0);
    }

    @Override
    public void updateZOffset(BlockState state) {
        if (state.get(TYPE) == LogBenchType.Offset.CENTER) {
            this.setZOffsetVoxel(0.0);
        } else {
            switch (state.get(FACING)) {
                case NORTH:
                    this.setZOffsetVoxel(-5.0);
                    break;
                case SOUTH:
                    this.setZOffsetVoxel(5.0);
                    break;
                default:
                    this.setZOffsetVoxel(0.0);
            }
        }
    }

    @Override
    public void updateXOffset(BlockState state) {
        if (state.get(TYPE) == LogBenchType.Offset.CENTER) {
            this.setXOffsetVoxel(0.0);
        } else {
            switch (state.get(FACING)) {
                case WEST:
                    this.setXOffsetVoxel(-5.0);
                    break;
                case EAST:
                    this.setXOffsetVoxel(5.0);
                    break;
                default:
                    this.setXOffsetVoxel(0.0);
            }
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient() && player.isSneaking() && player.getMainHandStack().isIn(ModItemTagProvider.STUMP_SHAPER)) {
            world.setBlockState(pos, state.cycle(TYPE));
            this.updateSitOffset(state);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        return Waterloggable.super.tryFillWithFluid(world, pos, state, fluidState);
    }

    @Override
    public boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return Waterloggable.super.canFillWithFluid(player, world, pos, state, fluid);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        if ((Boolean)state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING, WATERLOGGED);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        switch (type) {
            case LAND:
                return false;
            case WATER:
                return state.getFluidState().isIn(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }
}
