package net.midget807.cozycamps.item;

import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.collection.DefaultedList;

import java.util.Optional;

public class SackItem extends BlockItem {
    public SackItem(Block block, Settings settings) {
        super(block, settings);
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

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return !stack.contains(DataComponentTypes.HIDE_TOOLTIP) && !stack.contains(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(DataComponentTypes.CONTAINER)).map(SackTooltipData::new)
                : Optional.empty();
    }


    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }
}
