package net.midget807.cozycamps.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class SackScreenHandlerFactory implements NamedScreenHandlerFactory {
    private final ItemStack sack;

    public SackScreenHandlerFactory(ItemStack sack) {
        this.sack = sack;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Sack");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SackScreenHandler(syncId, playerInventory, sack);
    }
}
