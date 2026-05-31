package net.midget807.cozycamps.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.midget807.cozycamps.datagen.ModItemTagProvider;
import net.midget807.cozycamps.registry.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
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

public class StumpBlock extends SittableBlock implements Waterloggable {
    public static final MapCodec<StumpBlock> CODEC = createCodec(StumpBlock::new);
    public static final EnumProperty<StumpType.Size> SIZE = ModProperties.STUMP_SIZE;
    public static final EnumProperty<StumpType.Height> HEIGHT = ModProperties.STUMP_HEIGHT;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final VoxelShape SMALL_SHORT = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 4.0, 12.0);
    public static final VoxelShape SMALL_TALL = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    public static final VoxelShape MEDIUM_SHORT = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    public static final VoxelShape MEDIUM_TALL = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 8.0, 14.0);
    public static final VoxelShape LARGE_SHORT = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    public static final VoxelShape LARGE_TALL = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    private final ImmutableMap<BlockState, VoxelShape> shapes;

    public StumpBlock(Settings settings) {
        super(settings);
        this.shapes = getStateToShape(this.getStateManager());
        this.setDefaultState(this.getDefaultState().with(SIZE, StumpType.Size.LARGE).with(HEIGHT, StumpType.Height.TALL).with(WATERLOGGED, false));
        this.setYOffsetVoxel(8.0f);
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
                .collect(Collectors.toMap(Function.identity(), StumpBlock::getStateForShape));
        return ImmutableMap.copyOf(map);
    }

    private static VoxelShape getStateForShape(BlockState state) {
        StumpType.Size size = (StumpType.Size)state.get(SIZE);
        StumpType.Height height = (StumpType.Height)state.get(HEIGHT);
        switch (size) {
            case SMALL:
                if (height == StumpType.Height.SHORT) {
                    return SMALL_SHORT;
                } else {
                    return SMALL_TALL;
                }
            case MEDIUM:
                if (height == StumpType.Height.SHORT) {
                    return MEDIUM_SHORT;
                } else {
                    return MEDIUM_TALL;
                }
            case LARGE:
                if (height == StumpType.Height.SHORT) {
                    return LARGE_SHORT;
                } else {
                    return LARGE_TALL;
                }
            default:
                return LARGE_TALL;
        }
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean isWaterSource = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
        BlockState blockState = this.getDefaultState().with(WATERLOGGED, isWaterSource);
        return blockState;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient() && player.isSneaking() && player.getMainHandStack().isIn(ItemTags.AXES)) {
            if (hit.getSide() == Direction.UP || hit.getSide() == Direction.DOWN) {
                cycleHeight(world, pos, state);
            } else {
                cycleSize(world, pos, state);
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hit);
    }

    private void cycleHeight(World world, BlockPos pos, BlockState state) {
        if (state.get(HEIGHT) == StumpType.Height.TALL) {
            this.setYOffsetVoxel(4.0);
            world.setBlockState(pos, state.with(HEIGHT, StumpType.Height.SHORT));
        } else {
            this.setYOffsetVoxel(8.0);
            world.setBlockState(pos, state.with(HEIGHT, StumpType.Height.TALL));
        }
    }
    private void cycleSize(World world, BlockPos pos, BlockState state) {
        if (state.get(SIZE) == StumpType.Size.LARGE) {
            world.setBlockState(pos, state.with(SIZE, StumpType.Size.MEDIUM));
        } else if (state.get(SIZE) == StumpType.Size.MEDIUM) {
            world.setBlockState(pos, state.with(SIZE, StumpType.Size.SMALL));
        } else {
            world.setBlockState(pos, state.with(SIZE, StumpType.Size.LARGE));
        }
        this.updateYOffest(state);
    }

    @Override
    public void updateYOffest(BlockState state) {
        if (state.get(HEIGHT) == StumpType.Height.SHORT) {
            this.setYOffsetVoxel(4.0);
        } else if (state.get(HEIGHT) == StumpType.Height.TALL) {
            this.setYOffsetVoxel(8.0);
        }
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
        builder.add(SIZE, HEIGHT, WATERLOGGED);
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
