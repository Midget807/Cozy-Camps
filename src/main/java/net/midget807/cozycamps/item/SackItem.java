package net.midget807.cozycamps.item;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.midget807.cozycamps.item.component.SackInventoryComponent;
import net.midget807.cozycamps.network.C2S.payload.OpenSackScreenPayload;
import net.midget807.cozycamps.registry.ModDataComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class SackItem extends Item {
    public static final int MAX_SIZE = 9;

    public SackItem(Settings settings) {
        super(settings.component(ModDataComponents.SACK_INVENTORY, SackInventoryComponent.DEFAULT));
    }

    public static DefaultedList<ItemStack> getInventory(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SACK_INVENTORY, SackInventoryComponent.DEFAULT).getStacks();
    }

    public static void saveInventory(ItemStack stack, DefaultedList<ItemStack> inventory) {
        stack.set(ModDataComponents.SACK_INVENTORY, new SackInventoryComponent(List.copyOf(inventory)));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            ClientPlayNetworking.send(new OpenSackScreenPayload(stack));
        }
        return TypedActionResult.success(stack);
    }
}
