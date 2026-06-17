package net.midget807.cozycamps.block;

import com.mojang.serialization.MapCodec;
import net.midget807.cozycamps.datagen.ModItemTagProvider;
import net.midget807.cozycamps.registry.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class StakeBlock extends BlockWithEntity implements Waterloggable {
    public static final MapCodec<StakeBlock> CODEC = createCodec(StakeBlock::new);
    public static final EnumProperty<StakeType.Part> PART = ModProperties.STAKE_PART;
    public static final BooleanProperty LIT = Properties.LIT;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final VoxelShape BASE = Block.createCuboidShape(7, 0, 7, 9, 16, 9);
    public static final VoxelShape TOP = Block.createCuboidShape(7, 0, 7, 9, 8, 9);
    public static final VoxelShape POINT = VoxelShapes.union(
            Block.createCuboidShape(7.25, 0, 7.25, 8.75, 4, 8.75),
            Block.createCuboidShape(7.5, 4, 7.5, 8.5, 8, 8.5)
    );
    public static final VoxelShape COAL = VoxelShapes.union(
            Block.createCuboidShape(7, 0, 7, 9, 3, 9),
            Block.createCuboidShape(6.5, 3, 6.5, 9.5, 8, 9.5)
    );
    public static final VoxelShape HEAD = Block.createCuboidShape(4, 0, 4, 12, 8, 12);

    public StakeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false).with(LIT, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return state.get(PART) == StakeType.Part.BASE ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(PART)) {
            case BASE -> BASE;
            case TOP -> TOP;
            case COAL -> COAL;
            case POINT -> POINT;
            case HEAD -> HEAD;
        };
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED, LIT, PART);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient && player.isSneaking()) {
            if (state.get(PART) == StakeType.Part.TOP || state.get(PART) == StakeType.Part.POINT) {
                if (player.getMainHandStack().isIn(ModItemTagProvider.STUMP_SHAPER)) {
                    world.setBlockState(pos, state.with(LIT, false).with(PART, state.get(PART) == StakeType.Part.TOP ? StakeType.Part.POINT : StakeType.Part.TOP));
                    return ActionResult.SUCCESS;
                } else if (player.getMainHandStack().isIn(ItemTags.COALS)) {
                    world.setBlockState(pos, state.with(LIT, false).with(PART, StakeType.Part.COAL));
                    if (!player.isCreative()) {
                        player.getMainHandStack().decrement(1);
                    }
                    return ActionResult.SUCCESS;
                } else if (state.get(PART) == StakeType.Part.POINT && player.getMainHandStack().isIn(ItemTags.SKULLS)) {
                    world.setBlockState(pos, state.with(LIT, false).with(PART, StakeType.Part.HEAD));
                    return ActionResult.SUCCESS;
                }
            } else if (state.get(PART) == StakeType.Part.COAL) {
                if (player.getMainHandStack().isIn(ItemTags.SHOVELS)) {

                    if (state.get(LIT)) {
                        world.setBlockState(pos, state.with(LIT, false));
                        if (!player.isCreative()) {
                            player.getMainHandStack().decrement(1);
                        }
                    } else {
                        world.setBlockState(pos, state.with(PART, StakeType.Part.TOP).with(LIT, false));
                        player.giveItemStack(new ItemStack(Items.COAL));
                    }
                    return ActionResult.SUCCESS;
                }
            } else if (state.get(PART) == StakeType.Part.HEAD) {
                if (player.getMainHandStack().isIn(ItemTags.SHOVELS)) {
                    world.setBlockState(pos, state.with(LIT, false).with(PART, StakeType.Part.POINT));
                    world.removeBlockEntity(pos);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isOf(Items.FLINT_AND_STEEL) && state.get(PART) == StakeType.Part.COAL) {
            world.setBlockState(pos, state.with(LIT, true));
            if (!player.isCreative()) {
                stack.damage(1, player, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos upPos =  ctx.getBlockPos().offset(Direction.UP);
        World world = ctx.getWorld();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean isWaterSource = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
        return world.getBlockState(upPos).canReplace(ctx) && world.getWorldBorder().contains(upPos) ? this.getDefaultState().with(WATERLOGGED, isWaterSource).with(LIT, false).with(PART, StakeType.Part.BASE) : null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockPos upPos = pos.offset(Direction.UP);
        FluidState fluidState = world.getFluidState(upPos);
        boolean isWaterSource = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
        world.setBlockState(upPos, this.getDefaultState().with(WATERLOGGED, isWaterSource).with(LIT, false).with(PART, StakeType.Part.TOP));
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        StakeType.Part part = state.get(PART);
        BlockPos otherPos = part == StakeType.Part.BASE
                ? pos.offset(Direction.UP)
                : pos.offset(Direction.DOWN);
        if (part == StakeType.Part.HEAD) {
            world.removeBlockEntity(pos);
        }
        if (world.getBlockState(otherPos).get(PART) == StakeType.Part.HEAD) {
            world.removeBlockEntity(otherPos);
        }
        world.breakBlock(otherPos, false);
        super.onBreak(world, pos, state, player);
        return state;
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

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return state.get(PART) == StakeType.Part.HEAD ? new SkullBlockEntity(pos, state) : null;
    }
}
