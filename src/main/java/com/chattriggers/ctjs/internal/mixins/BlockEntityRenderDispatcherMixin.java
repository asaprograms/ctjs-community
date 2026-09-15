package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.internal.engine.CTEvents;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    private static final Map<BlockEntityRenderState, ExtractedBlockEntity> ctjs$extractedBlockEntities =
        Collections.synchronizedMap(new WeakHashMap<>());

    @Inject(method = "tryExtractRenderState", at = @At("RETURN"))
    private <E extends BlockEntity, S extends BlockEntityRenderState> void ctjs$captureBlockEntity(
        E blockEntity,
        float partialTicks,
        ModelFeatureRenderer.CrumblingOverlay breakProgress,
        boolean render,
        CallbackInfoReturnable<S> cir
    ) {
        S state = cir.getReturnValue();
        if (state != null) {
            ctjs$extractedBlockEntities.put(state, new ExtractedBlockEntity(blockEntity, partialTicks));
        }
    }

    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private <S extends BlockEntityRenderState> void ctjs$renderBlockEntity(
        S renderState,
        PoseStack matrices,
        SubmitNodeCollector queue,
        CameraRenderState cameraRenderState,
        CallbackInfo ci
    ) {
        ExtractedBlockEntity extracted = ctjs$extractedBlockEntities.get(renderState);
        if (extracted != null) {
            CTEvents.RENDER_BLOCK_ENTITY.invoker().render(matrices, extracted.blockEntity(), extracted.partialTicks(), ci);
        }
    }

    @Inject(method = "submit", at = @At("RETURN"))
    private <S extends BlockEntityRenderState> void ctjs$postRenderBlockEntity(
        S renderState,
        PoseStack matrices,
        SubmitNodeCollector queue,
        CameraRenderState cameraRenderState,
        CallbackInfo ci
    ) {
        ExtractedBlockEntity extracted = ctjs$extractedBlockEntities.get(renderState);
        if (extracted != null) {
            double x = renderState.blockPos.getX() - cameraRenderState.pos.x;
            double y = renderState.blockPos.getY() - cameraRenderState.pos.y;
            double z = renderState.blockPos.getZ() - cameraRenderState.pos.z;
            CTEvents.POST_RENDER_BLOCK_ENTITY.invoker().render(
                matrices, extracted.blockEntity(), x, y, z, extracted.partialTicks()
            );
        }
    }

    private record ExtractedBlockEntity(BlockEntity blockEntity, float partialTicks) {}
}
