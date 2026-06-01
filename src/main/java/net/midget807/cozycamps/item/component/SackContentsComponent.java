package net.midget807.cozycamps.item.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.midget807.cozycamps.registry.ModDataComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public final class SackContentsComponent implements TooltipData {
    public static final SackContentsComponent DEFAULT = new SackContentsComponent(List.of());
    public static final Codec<SackContentsComponent> CODEC = ItemStack.CODEC.listOf().xmap(SackContentsComponent::new, component -> component.stacks);
    public static final PacketCodec<RegistryByteBuf, SackContentsComponent> PACKET_CODEC = ItemStack.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(SackContentsComponent::new, component -> component.stacks);
    private static final int ADD_TO_NEW_SLOT = -1;
    private final List<ItemStack> stacks;
    private final Fraction occupancy;

    public SackContentsComponent(List<ItemStack> stacks, Fraction occupancy) {
        this.stacks = stacks;
        this.occupancy = occupancy;
    }

    public SackContentsComponent(List<ItemStack> stacks) {
        this(stacks, calculateOccupancy(stacks));
    }

    private static Fraction calculateOccupancy(List<ItemStack> stacks) {
        Fraction fraction = Fraction.ZERO;
        for (ItemStack itemStack : stacks) {
            if (itemStack != null && !itemStack.isEmpty()) {
                fraction = fraction.add(Fraction.getFraction(1, 9));
            }
        }
        return fraction;
    }

    public ItemStack get(int index) {
        return this.stacks.get(index);
    }

    public int size() {
        return this.stacks.size();
    }

    public Stream<ItemStack> stream() {
        return this.stacks.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> iterate() {
        return this.stacks;
    }

    public Iterable<ItemStack> iterateCopy() {
        return Lists.transform(this.stacks, ItemStack::copy);
    }

    public Fraction getOccupancy() {
        return this.occupancy;
    }

    public boolean isEmpty() {
        return this.stacks.isEmpty();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return !(o instanceof SackContentsComponent sackContentsComponent)
                    ? false
                    : this.occupancy.equals(sackContentsComponent.occupancy) && ItemStack.stacksEqual(this.stacks, sackContentsComponent.stacks);
        }
    }

    public int hashCode() {
        return ItemStack.listHashCode(this.stacks);
    }

    public String toString() {
        return "SackContents" + this.stacks;
    }

    public static class Builder {
        private final List<ItemStack> stacks;
        private Fraction occupancy;

        public Builder(SackContentsComponent base) {
            this.stacks = new ArrayList<>(base.stacks);
            this.occupancy = base.occupancy;
        }

        public SackContentsComponent.Builder clear() {
            this.stacks.clear();
            this.occupancy = Fraction.ZERO;
            return this;
        }

        private int addInternal(ItemStack stack) {
            if (stack.isStackable()) {
                return -1;
            } else {
                return 1;
            }
        }

        private int getMaxAllowed(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.occupancy);
            return fraction.compareTo(Fraction.ZERO) <= 0 ? 0 : 1;
        }

        public int add(ItemStack stack) {
            if (!stack.isEmpty() && stack.getItem().canBeNested()) {
                int amountToAdd = this.getMaxAllowed(stack);
                if (amountToAdd == 0) {
                    return 0;
                } else {
                    this.occupancy = this.occupancy.add(Fraction.getFraction(1, 9));
                    int internal = this.addInternal(stack);
                    if (internal != -1) {
                        this.stacks.add(0, stack);
                    }
                    return amountToAdd;
                }
            } else {
                return 0;
            }
        }

        public int add(Slot slot, PlayerEntity player) {
            ItemStack itemStack = slot.getStack();
            int i = this.getMaxAllowed(itemStack);
            return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
        }

        @Nullable
        public ItemStack removeFirst() {
            if (this.stacks.isEmpty()) {
                return null;
            } else {
                ItemStack itemStack = ((ItemStack) this.stacks.remove(0)).copy();
                this.occupancy = this.occupancy.subtract(Fraction.getFraction(1, 9));
                return itemStack;
            }
        }

        public Fraction getOccupancy() {
            return this.occupancy;
        }

        public SackContentsComponent build() {
            return new SackContentsComponent(List.copyOf(this.stacks), this.occupancy);
        }
    }
}
