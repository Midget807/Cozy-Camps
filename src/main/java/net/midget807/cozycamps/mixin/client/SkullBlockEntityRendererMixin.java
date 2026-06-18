package net.midget807.cozycamps.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.cozycamps.block.StakeBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SkullBlockEntityRenderer.class)
public abstract class SkullBlockEntityRendererMixin implements BlockEntityRenderer<SkullBlockEntity> {
    @Shadow
    @Final
    private Map<SkullBlock.SkullType, SkullBlockEntityModel> MODELS;

    @Shadow
    public static RenderLayer getRenderLayer(SkullBlock.SkullType type, @Nullable ProfileComponent profile) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    public static void renderSkull(@Nullable Direction direction, float yaw, float animationProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SkullBlockEntityModel model, RenderLayer renderLayer) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(method = "render(Lnet/minecraft/block/entity/SkullBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/RotationPropertyHelper;toDegrees(I)F", shift = At.Shift.AFTER), cancellable = true)
    private void cozycamps$renderWithStake(SkullBlockEntity skullBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci, @Local BlockState blockState, @Local Direction direction, @Local(argsOnly = false, ordinal = 1) float g, @Local(argsOnly = false, ordinal = 2) int k) {
        if (blockState.getBlock() instanceof StakeBlock stakeBlock) {
            SkullBlock.SkullType skullType = blockState.get(StakeBlock.SKULL_TYPE);;
            float h = RotationPropertyHelper.toDegrees(k);
            SkullBlockEntityModel skullBlockEntityModel = (SkullBlockEntityModel) this.MODELS.get(skullType);
            RenderLayer renderLayer = getRenderLayer(skullType, skullBlockEntity.getOwner() == null ? null : skullBlockEntity.getOwner());
            renderSkull(direction, h, g, matrixStack, vertexConsumerProvider, i, skullBlockEntityModel, renderLayer);
            ci.cancel();
            return;
        }
    }
}
