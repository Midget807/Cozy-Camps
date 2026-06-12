package net.midget807.cozycamps.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SingleItemSlot extends Slot {
    public SingleItemSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem().canBeNested();
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return 1;
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
