package net.midget807.cozycamps.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.midget807.cozycamps.block.entity.SackBlockEntity;
import net.midget807.cozycamps.registry.ModBlockEntities;
import net.midget807.cozycamps.registry.ModBlocks;
import net.midget807.cozycamps.registry.ModStats;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SackBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<SackBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(DyeColor.CODEC.optionalFieldOf("color").forGetter(block -> Optional.ofNullable(block.color)), createSettingsCodec())
                    .apply(instance, (dyeColor, settings1) -> new SackBlock((DyeColor) dyeColor.orElse(null), settings1))
    );
    public static final Text UNKNOWN_CONTENTS_TEXT = Text.translatable("container.shulkerBox.unknownContents");
    public static final VoxelShape SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 8.0, 11.0);
    @Nullable
    private final DyeColor color;

    public SackBlock(@Nullable DyeColor color, Settings settings) {
        super(settings);
        this.color = color;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SackBlockEntity(this.color, pos, state);
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
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        if (stack.contains(DataComponentTypes.CONTAINER_LOOT)) {
            tooltip.add(UNKNOWN_CONTENTS_TEXT);
        }
        int i = 0;
        int j = 0;

        for (ItemStack itemStack : stack.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT).iterateNonEmpty()) {
            j++;
            if (i <= 4) {
                i++;
                tooltip.add(Text.translatable("container.shulkerBox.itemCount", itemStack.getName(), itemStack.getCount()));
            }
        }

        if (j - i > 0) {
            tooltip.add(Text.translatable("container.shulkerBox.more", j - i).formatted(Formatting.ITALIC));
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
        return new ItemStack(get(color));
    }
}
