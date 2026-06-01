package net.midget807.cozycamps.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SackInventoryComponent {
    public static final SackInventoryComponent DEFAULT = new SackInventoryComponent(List.of());
    public static final Codec<SackInventoryComponent> CODEC = ItemStack.CODEC.listOf().xmap(SackInventoryComponent::new, component -> component.items);
    public static final PacketCodec<RegistryByteBuf, SackInventoryComponent> PACKET_CODEC = ItemStack.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(SackInventoryComponent::new, component -> component.items);

    private final List<ItemStack> items;
    private final DefaultedList<ItemStack> inventory;

    public SackInventoryComponent(List<ItemStack> items) {
        this.items = items;
        this.inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    }

    public DefaultedList<ItemStack> getStacks() {
        return this.inventory;
    }

    public int add(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().canBeNested()) {
            int stackCount = stack.getCount();
            if (this.inventory.contains(ItemStack.EMPTY)) {
                this.inventory.set(0, stack.copyWithCount(1));
                stack.decrement(1);
                return 1;
            }
        }
        return 0;
    }

    @Nullable
    public ItemStack removeFirst() {
        if (this.inventory.isEmpty()) {
            return null;
        } else {
            ItemStack itemStack = this.inventory.removeFirst();
            return itemStack;
        }
    }

    public SackInventoryComponent build() {
        return new SackInventoryComponent(List.copyOf(this.inventory));
    }
}
