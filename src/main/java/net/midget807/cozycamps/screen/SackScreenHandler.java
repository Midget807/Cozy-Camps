package net.midget807.cozycamps.screen;

import net.midget807.cozycamps.registry.ModScreenHandlers;
import net.midget807.cozycamps.screen.slot.SingleItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
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

        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 3; ++l) {
                this.addSlot(new SingleItemSlot(this.inventory, l + i * 9 + 9, 26 + l * 18, 78 + i * 18));
            }
        }
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 26 + l * 18, 140 + i * 18));
            }
        }
    }

    public void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 26 + i * 18, 198));
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
    }
}
