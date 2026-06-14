package net.midget807.cozycamps.item.component;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.midget807.cozycamps.item.SackItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class SackInventoryComponent {
    public static final Codec<SackInventoryComponent> CODEC = Slot.CODEC
            .sizeLimitedListOf(9)
            .xmap(SackInventoryComponent::fromSlots, SackInventoryComponent::collectSlots);
    public static final PacketCodec<RegistryByteBuf, SackInventoryComponent> PACKET_CODEC = ItemStack.OPTIONAL_PACKET_CODEC
            .collect(PacketCodecs.toList(9))
            .xmap(SackInventoryComponent::new, component -> component.inventory);
    public static final SackInventoryComponent DEFAULT = new SackInventoryComponent(DefaultedList.ofSize(SackItem.INVENTORY_SIZE, ItemStack.EMPTY));
    private final DefaultedList<ItemStack> inventory;

    public SackInventoryComponent(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }
    public SackInventoryComponent() {
        this(DefaultedList.ofSize(SackItem.INVENTORY_SIZE, ItemStack.EMPTY));
    }
    public SackInventoryComponent(List<ItemStack> stacks) {
        this();

        for (int i = 0; i < SackItem.INVENTORY_SIZE; i++) {
            if (i >= stacks.size()) break;
            this.inventory.set(i, stacks.get(i));
        }
    }

    public static SackInventoryComponent fromStacks(@Nullable List<ItemStack> stacks) {
        if (stacks == null) {
            return DEFAULT;
        } else {
            SackInventoryComponent sackInventoryComponent = new SackInventoryComponent();

            for (int i = 0; i < SackItem.INVENTORY_SIZE; i++) {
                if (i >= stacks.size()) break;
                sackInventoryComponent.inventory.set(i, stacks.get(i));
            }

            return sackInventoryComponent;
        }
    }

    public DefaultedList<ItemStack> copy() {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(SackItem.INVENTORY_SIZE, ItemStack.EMPTY);
        for (int i = 0; i < SackItem.INVENTORY_SIZE; i++) {
            list.set(i, this.inventory.get(i).copy());
        }
        return list;
    }

    public void removeStack(int slot) {
        this.inventory.remove(slot);
    }

    public void setStack(int slot, @Nullable ItemStack stack) {
        if (stack == null) {
            this.inventory.set(slot, ItemStack.EMPTY);
        } else {
            this.inventory.set(slot, stack);
        }
    }

    public DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    private static SackInventoryComponent fromSlots(List<Slot> slots) {
        OptionalInt optionalInt = slots.stream().mapToInt(Slot::index).max();
        if (optionalInt.isEmpty()) {
            return DEFAULT;
        } else {
            SackInventoryComponent sackInventoryComponent = new SackInventoryComponent();

            for (Slot slot : slots) {
                sackInventoryComponent.inventory.set(slot.index, slot.item);
            }

            return sackInventoryComponent;
        }
    }

    private List<Slot> collectSlots() {
        List<Slot> list = new ArrayList();

        for (int i = 0; i < SackItem.INVENTORY_SIZE; i++) {
            ItemStack itemStack = this.inventory.get(i);
            if (!itemStack.isEmpty()) {
                list.add(new Slot(i, itemStack));
            }
        }

        return list;
    }

    public Iterable<ItemStack> iterateNonEmpty() {
        return Iterables.filter(this.inventory, stack -> !stack.isEmpty());
    }

    public record Slot(int index, ItemStack item) {
        public static final Codec<Slot> CODEC = RecordCodecBuilder.create(
                slotInstance -> slotInstance.group(
                        Codec.intRange(0, 8).fieldOf("slot").forGetter(Slot::index),
                        ItemStack.CODEC.fieldOf("item").forGetter(Slot::item)
                ).apply(slotInstance, Slot::new)
        );
    }
}
