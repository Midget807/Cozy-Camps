package net.midget807.cozycamps.item;

import net.midget807.cozycamps.block.SackBlock;
import net.midget807.cozycamps.block.entity.SackBlockEntity;
import net.midget807.cozycamps.item.component.SackInventoryComponent;
import net.midget807.cozycamps.registry.ModDataComponents;
import net.midget807.cozycamps.screen.SackScreenHandler;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class SackItem extends BlockItem {
    public static final int INVENTORY_SIZE = 9;
    public final int[] AVAILABLE_SLOTS = IntStream.range(0, INVENTORY_SIZE).toArray();
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

    public SackItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            user.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, playerInventory, player) -> new SackScreenHandler(
                            syncId, playerInventory, Inventory.fromItem(stack)
                    ),
                    Text.literal("")
            ));
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        if (!this.getBlock().isEnabled(context.getWorld().getEnabledFeatures())) {
            return ActionResult.FAIL;
        } else if (!context.canPlace()) {
            return ActionResult.FAIL;
        } else {
            ItemPlacementContext itemPlacementContext = this.getPlacementContext(context);
            if (itemPlacementContext == null) {
                return ActionResult.FAIL;
            } else {
                BlockState blockState = this.getPlacementState(itemPlacementContext);
                if (blockState == null) {
                    return ActionResult.FAIL;
                } else if (!this.place(itemPlacementContext, blockState)) {
                    return ActionResult.FAIL;
                } else {
                    BlockPos blockPos = itemPlacementContext.getBlockPos();
                    World world = itemPlacementContext.getWorld();
                    PlayerEntity playerEntity = itemPlacementContext.getPlayer();
                    ItemStack itemStack = itemPlacementContext.getStack();
                    BlockState blockState2 = world.getBlockState(blockPos);
                    if (blockState2.isOf(blockState.getBlock())) {
                        blockState2 = this.placeFromNbt(blockPos, world, itemStack, blockState2);
                        this.postPlacement(blockPos, world, playerEntity, itemStack, blockState2);
                        copyComponentsToBlockEntity(world, blockPos, itemStack);
                        blockState2.getBlock().onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
                        if (playerEntity instanceof ServerPlayerEntity) {
                            Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
                        }
                    }

                    BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
                    world.playSound(
                            playerEntity,
                            blockPos,
                            this.getPlaceSound(blockState2),
                            SoundCategory.BLOCKS,
                            (blockSoundGroup.getVolume() + 1.0F) / 2.0F,
                            blockSoundGroup.getPitch() * 0.8F
                    );
                    world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(playerEntity, blockState2));
                    itemStack.decrementUnlessCreative(1, playerEntity);
                    return ActionResult.success(world.isClient);
                }
            }
        }
    }
    private BlockState placeFromNbt(BlockPos pos, World world, ItemStack stack, BlockState state) {
        BlockStateComponent blockStateComponent = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
        if (blockStateComponent.isEmpty()) {
            return state;
        } else {
            BlockState blockState = blockStateComponent.applyToState(state);
            if (blockState != state) {
                world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            }

            return blockState;
        }
    }
    private static void copyComponentsToBlockEntity(World world, BlockPos pos, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null) {
            blockEntity.readComponents(stack);
            blockEntity.markDirty();
        }
    }


    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            ContainerComponent containerComponent = stack.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
            if (containerComponent == null) {
                return false;
            } else {
                ItemStack slotStack = slot.getStack();
                ContainerComponent containerComponent2 = containerComponent;
                if (slotStack.isEmpty()) {
                    this.playRemoveOneSound(player);
                    ItemStack containerStack = containerComponent.copyFirstStack();
                    if (containerStack != null) {
                        ItemStack newStack = slot.insertStack(containerStack);
                        DefaultedList<ItemStack> container = DefaultedList.of();
                        containerComponent.copyTo(container);
                        container.removeFirst();
                        containerComponent2 = ContainerComponent.fromStacks(container);
                    }
                } else if (slotStack.getItem().canBeNested()) {
                    int remainderCount = slotStack.getCount() - 1;
                    DefaultedList<ItemStack> container = DefaultedList.ofSize(9, ItemStack.EMPTY);
                    containerComponent.copyTo(container);
                    if (container.contains(ItemStack.EMPTY)) {
                        this.playInsertSound(player);
                        if (remainderCount > 0) {
                            slot.setStack(slotStack.copyWithCount(remainderCount));
                        } else {
                            slot.setStack(ItemStack.EMPTY);
                        }
                        DefaultedList<ItemStack> container2 = DefaultedList.ofSize(9, ItemStack.EMPTY);
                        container.add(0, slotStack.copyWithCount(1));
                        containerComponent2 = ContainerComponent.fromStacks(container);
                    }
                }
                stack.set(DataComponentTypes.CONTAINER, containerComponent2);
                return true;
            }
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    public static DefaultedList<ItemStack> readInventory(ItemStack stack) {
        SackInventoryComponent sackInventoryComponent = stack.getOrDefault(ModDataComponents.SACK_INVENTORY, SackInventoryComponent.DEFAULT);
        return sackInventoryComponent.getHeldStacks();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (stack.contains(DataComponentTypes.CONTAINER_LOOT)) {
            tooltip.add(SackBlock.UNKNOWN_CONTENTS_TEXT);
        }
        int i = 0;
        int j = 0;

        for (ItemStack itemStack : stack.getOrDefault(ModDataComponents.SACK_INVENTORY, SackInventoryComponent.DEFAULT).iterateNonEmpty()) {
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

    public static class Inventory extends SimpleInventory {
        public Inventory() {
            super(INVENTORY_SIZE);
        }

        public static SimpleInventory fromItem(ItemStack stack) {
            SackInventoryComponent sackInventoryComponent = stack.getOrDefault(ModDataComponents.SACK_INVENTORY, SackInventoryComponent.DEFAULT);
            SimpleInventory simpleInventory = new SimpleInventory(9);
            for (int i = 0; i < sackInventoryComponent.getHeldStacks().size(); i++) {
                simpleInventory.setStack(i, sackInventoryComponent.getHeldStacks().get(i));
            }
            return simpleInventory;
        }
    }
}
