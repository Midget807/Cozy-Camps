package net.midget807.cozycamps.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.cozycamps.datagen.ModBlockTagProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @WrapOperation(method = "interactBlockInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;shouldCancelInteraction()Z"))
    private boolean cozyCamps$allowSneakBlockInteraction(ClientPlayerEntity instance, Operation<Boolean> original, @Local(argsOnly = true)BlockHitResult hitResult) {
        if (instance.getWorld().getBlockState(hitResult.getBlockPos()).isIn(ModBlockTagProvider.SNEAK_INTERACTABLE)) {
            return false;
        } else {
            return original.call(instance);
        }
    }
}
