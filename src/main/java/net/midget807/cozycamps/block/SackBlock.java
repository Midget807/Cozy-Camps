package net.midget807.cozycamps.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.midget807.cozycamps.block.entity.SackBlockEntity;
import net.midget807.cozycamps.registry.ModBlockEntities;
import net.midget807.cozycamps.registry.ModBlocks;
import net.midget807.cozycamps.registry.ModItems;
import net.midget807.cozycamps.registry.ModStats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SackBlock extends BlockWithEntity implements BlockEntityProvider, Waterloggable {
    public static final MapCodec<SackBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(DyeColor.CODEC.optionalFieldOf("color").forGetter(block -> Optional.ofNullable(block.color)), createSettingsCodec())
                    .apply(instance, (dyeColor, settings1) -> new SackBlock((DyeColor) dyeColor.orElse(null), settings1))
    );
    public static final Text UNKNOWN_CONTENTS_TEXT = Text.translatable("container.shulkerBox.unknownContents");
    public static final VoxelShape SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 8.0, 11.0);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    @Nullable
    private final DyeColor color;

    public SackBlock(@Nullable DyeColor color, Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
        this.color = color;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        SackBlockEntity sackBlockEntity = new SackBlockEntity(this.color, pos, state);
        return sackBlockEntity;
    }

    @Override
    public Item asItem() {
        if (this.color == null) {
            return ModItems.SACK;
        } else {
            return switch (color) {
                case WHITE -> ModItems.WHITE_SACK;
                case ORANGE -> ModItems.ORANGE_SACK;
                case MAGENTA -> ModItems.MAGENTA_SACK;
                case LIGHT_BLUE -> ModItems.LIGHT_BLUE_SACK;
                case YELLOW -> ModItems.YELLOW_SACK;
                case LIME -> ModItems.LIME_SACK;
                case PINK -> ModItems.PINK_SACK;
                case GRAY -> ModItems.GRAY_SACK;
                case LIGHT_GRAY -> ModItems.LIGHT_GRAY_SACK;
                case CYAN -> ModItems.CYAN_SACK;
                case BLUE -> ModItems.BLUE_SACK;
                case BROWN -> ModItems.BROWN_SACK;
                case GREEN -> ModItems.GREEN_SACK;
                case RED -> ModItems.RED_SACK;
                case BLACK -> ModItems.BLACK_SACK;
                case PURPLE -> ModItems.PURPLE_SACK;
            };
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.SACK, SackBlockEntity::tick);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return ActionResult.CONSUME;
        } else if (world.getBlockEntity(pos) instanceof SackBlockEntity sackBlockEntity) {
            player.openHandledScreen(sackBlockEntity);
            player.incrementStat(ModStats.OPEN_SACK);

            return ActionResult.CONSUME;
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SackBlockEntity sackBlockEntity) {
            if (!world.isClient && player.isCreative() && !sackBlockEntity.isEmpty()) {
                ItemStack itemStack = getItemStack(this.getColor());
                itemStack.applyComponentsFrom(blockEntity.createComponentMap());
                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            } else {
                sackBlockEntity.generateLoot(player);
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof SackBlockEntity sackBlockEntity) {
            builder = builder.addDynamicDrop(Identifier.ofVanilla("contents"), lootConsumer -> {
                for (int i = 0; i < sackBlockEntity.size(); i++) {
                    lootConsumer.accept(sackBlockEntity.getStack(i));
                }
            });
        }
        return super.getDroppedStacks(state, builder);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            super.onStateReplaced(state, world, pos, newState, moved);
            if (blockEntity instanceof SackBlockEntity) {
                world.updateComparators(pos, state.getBlock());
            }
        }
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getPickStack(world, pos, state);
        world.getBlockEntity(pos, ModBlockEntities.SACK).ifPresent(blockEntity -> blockEntity.setStackNbt(itemStack, world.getRegistryManager()));
        return itemStack;
    }

    public static @Nullable DyeColor getColor(Item item) {
        return getColor(Block.getBlockFromItem(item));
    }

    public static @Nullable DyeColor getColor(Block block) {
        return block instanceof SackBlock ? ((SackBlock) block).getColor() : null;
    }

    public static Block get(@Nullable DyeColor color) {
        if (color == null) {
            return ModBlocks.SACK;
        } else {
            return switch (color) {
                case WHITE -> ModBlocks.WHITE_SACK;
                case ORANGE -> ModBlocks.ORANGE_SACK;
                case MAGENTA -> ModBlocks.MAGENTA_SACK;
                case LIGHT_BLUE -> ModBlocks.LIGHT_BLUE_SACK;
                case YELLOW -> ModBlocks.YELLOW_SACK;
                case LIME -> ModBlocks.LIME_SACK;
                case PINK -> ModBlocks.PINK_SACK;
                case GRAY -> ModBlocks.GRAY_SACK;
                case LIGHT_GRAY -> ModBlocks.LIGHT_GRAY_SACK;
                case CYAN -> ModBlocks.CYAN_SACK;
                case BLUE -> ModBlocks.BLUE_SACK;
                case BROWN -> ModBlocks.BROWN_SACK;
                case GREEN -> ModBlocks.GREEN_SACK;
                case RED -> ModBlocks.RED_SACK;
                case BLACK -> ModBlocks.BLACK_SACK;
                case PURPLE -> ModBlocks.PURPLE_SACK;
            };
        }
    }

    public @Nullable DyeColor getColor() {
        return this.color;
    }

    public static ItemStack getItemStack(@Nullable DyeColor color) {
        Block block = get(color);
        Item item = block.asItem();
        ItemStack itemStack = new ItemStack(block);
        /*if (item instanceof SackItem sackItem) {
            itemStack = new ItemStack(sackItem);
            itemStack.set(ModDataComponents.SACK_INVENTORY, SackInventoryComponent.fromStacks())
        }*/
        return itemStack;
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
        builder.add(FACING, WATERLOGGED);
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
