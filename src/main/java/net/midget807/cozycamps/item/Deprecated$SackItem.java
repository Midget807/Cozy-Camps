package net.midget807.cozycamps.item;

import net.midget807.cozycamps.item.component.SackContentsComponent;
import net.midget807.cozycamps.registry.ModDataComponents;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;
import java.util.Optional;

public class Deprecated$SackItem extends BlockItem {
    private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4F, 0.4F, 1.0F);

    public Deprecated$SackItem(Block block, Settings settings) {
        super(block, settings);
    }
    
    public static float getAmountFilled(ItemStack stack) {
        SackContentsComponent sackContentsComponent = stack.getOrDefault(ModDataComponents.SACK_CONTENTS, SackContentsComponent.DEFAULT);
        return sackContentsComponent.getOccupancy().floatValue();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.FAIL;
        /*if (context.getPlayer().isSneaking()) {
            return super.useOnBlock(context);
        } else {
        }*/
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            SackContentsComponent sackContentsComponent = stack.get(ModDataComponents.SACK_CONTENTS);
            if (sackContentsComponent == null) {
                return false;
            } else {
                ItemStack itemStack = slot.getStack();
                SackContentsComponent.Builder builder = new SackContentsComponent.Builder(sackContentsComponent);
                if (itemStack.isEmpty()) {
                    this.playRemoveOneSound(player);
                    ItemStack itemStack2 = builder.removeFirst();
                    if (itemStack2 != null) {
                        ItemStack itemStack3 = slot.insertStack(itemStack2);
                        builder.add(itemStack3);
                    }
                } else if (itemStack.getItem().canBeNested()) {
                    int i = builder.add(slot, player);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(ModDataComponents.SACK_CONTENTS, builder.build());
                return true;
            }
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            SackContentsComponent sackContentsComponent = stack.get(ModDataComponents.SACK_CONTENTS);
            if (sackContentsComponent == null) {
                return false;
            } else {
                SackContentsComponent.Builder builder = new SackContentsComponent.Builder(sackContentsComponent);
                if (otherStack.isEmpty()) {
                    ItemStack itemStack = builder.removeFirst();
                    if (itemStack != null) {
                        this.playRemoveOneSound(player);
                        cursorStackReference.set(itemStack);
                    }
                } else {
                    int i = builder.add(otherStack);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(ModDataComponents.SACK_CONTENTS, builder.build());
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        SackContentsComponent sackContentsComponent = stack.getOrDefault(ModDataComponents.SACK_CONTENTS, SackContentsComponent.DEFAULT);
        return sackContentsComponent.getOccupancy().compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        SackContentsComponent sackContentsComponent = stack.getOrDefault(ModDataComponents.SACK_CONTENTS, SackContentsComponent.DEFAULT);
        return Math.min(1 + MathHelper.multiplyFraction(sackContentsComponent.getOccupancy(), 12), 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return !stack.contains(DataComponentTypes.HIDE_TOOLTIP) && !stack.contains(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(ModDataComponents.SACK_CONTENTS)).map(SackTooltipData::new)
                : Optional.empty();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        SackContentsComponent sackContentsComponent = stack.get(ModDataComponents.SACK_CONTENTS);
        if (sackContentsComponent != null) {
            int i = MathHelper.multiplyFraction(sackContentsComponent.getOccupancy(), 9);
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", i, 9).formatted(Formatting.GRAY));
        }
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        SackContentsComponent sackContentsComponent = entity.getStack().get(ModDataComponents.SACK_CONTENTS);
        if (sackContentsComponent != null) {
            entity.getStack().set(ModDataComponents.SACK_CONTENTS, SackContentsComponent.DEFAULT);
            ItemUsage.spawnItemContents(entity, sackContentsComponent.iterateCopy());
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }
}
