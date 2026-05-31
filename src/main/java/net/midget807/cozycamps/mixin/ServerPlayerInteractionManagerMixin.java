package net.midget807.cozycamps.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.cozycamps.datagen.ModBlockTagProvider;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @WrapOperation(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;shouldCancelInteraction()Z"))
    private boolean cozyCamps$allowSneakBlockInteraction(ServerPlayerEntity instance, Operation<Boolean> original, @Local(argsOnly = true) BlockHitResult hitResult) {
        if (instance.getWorld().getBlockState(hitResult.getBlockPos()).isIn(ModBlockTagProvider.SNEAK_INTERACTABLE)) {
            return false;
        } else {
            return original.call(instance);
        }
    }
}
