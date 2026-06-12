package net.midget807.cozycamps.item;

import net.midget807.cozycamps.block.entity.SackBlockEntity;
import net.midget807.cozycamps.item.component.SackInventoryComponent;
import net.midget807.cozycamps.registry.ModDataComponents;
import net.midget807.cozycamps.screen.SackScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
