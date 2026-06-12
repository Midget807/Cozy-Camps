package net.midget807.cozycamps.screen;

import net.midget807.cozycamps.item.component.SackInventoryComponent;
import net.midget807.cozycamps.registry.ModDataComponents;
import net.midget807.cozycamps.registry.ModItems;
import net.midget807.cozycamps.registry.ModScreenHandlers;
import net.midget807.cozycamps.screen.slot.SingleItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.ShulkerBoxSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public class SackScreenHandler extends ScreenHandler {
    public static final int INVENTORY_SIZE = 9;
    public final Inventory inventory;
    public final PlayerEntity player;

    public SackScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(INVENTORY_SIZE));
    }

    public SackScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.SACK, syncId);
        checkSize(inventory, INVENTORY_SIZE);
        this.player = playerInventory.player;
        this.inventory = inventory;

        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                this.addSlot(new SingleItemSlot(inventory, l + k * 3, 8 + l * 18 + (18 * 3), 18 + k * 18));
            }
        }
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public void addPlayerInventory(PlayerInventory playerInventory) {
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInventory, l + k * 9 + 9, 8 + l * 18, 84 + k * 18));
            }
        }
    }

    public void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }



    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
        ItemStack itemStack = player.getStackInHand(player.getActiveHand());
        if (itemStack.isOf(ModItems.SACK)) {
            DefaultedList<ItemStack> stacks = DefaultedList.ofSize(9, ItemStack.EMPTY);
            for (int i = 0; i < inventory.size(); i++) {
                stacks.set(i, inventory.getStack(i));
            }
            itemStack.set(ModDataComponents.SACK_INVENTORY, SackInventoryComponent.fromStacks(stacks));
        }
    }
}
