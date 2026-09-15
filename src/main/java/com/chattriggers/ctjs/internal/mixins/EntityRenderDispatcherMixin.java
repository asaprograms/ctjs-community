package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.api.render.Renderer;
import com.chattriggers.ctjs.internal.engine.CTEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    private static final Map<EntityRenderState, ExtractedEntity> ctjs$extractedEntities =
        Collections.synchronizedMap(new WeakHashMap<>());

    @Inject(method = "onResourceManagerReload", at = @At("TAIL"))
    private void injectReload(ResourceManager manager, CallbackInfo ci, @Local EntityRendererProvider.Context context) {
        Renderer.initializePlayerRenderers$ctjs(context);
    }

    @Inject(method = "extractEntity", at = @At("RETURN"))
    private <E extends Entity> void ctjs$captureEntity(E entity, float partialTicks, CallbackInfoReturnable<EntityRenderState> cir) {
        EntityRenderState state = cir.getReturnValue();
        if (state != null) {
            ctjs$extractedEntities.put(state, new ExtractedEntity(entity, partialTicks));
        }
    }

    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private <S extends EntityRenderState> void ctjs$renderEntity(
        S renderState,
        net.minecraft.client.renderer.state.level.CameraRenderState cameraRenderState,
        double x,
        double y,
        double z,
        PoseStack matrixStack,
        SubmitNodeCollector queue,
        CallbackInfo ci
    ) {
        ExtractedEntity extracted = ctjs$extractedEntities.get(renderState);
        if (extracted != null) {
            CTEvents.RENDER_ENTITY.invoker().render(matrixStack, extracted.entity(), extracted.partialTicks(), ci);
        }
    }

    @Inject(method = "submit", at = @At("RETURN"))
    private <S extends EntityRenderState> void ctjs$postRenderEntity(
        S renderState,
        net.minecraft.client.renderer.state.level.CameraRenderState cameraRenderState,
        double x,
        double y,
        double z,
        PoseStack matrixStack,
        SubmitNodeCollector queue,
        CallbackInfo ci
    ) {
        ExtractedEntity extracted = ctjs$extractedEntities.get(renderState);
        if (extracted != null) {
            CTEvents.POST_RENDER_ENTITY.invoker().render(matrixStack, extracted.entity(), x, y, z, extracted.partialTicks());
        }
    }

    private record ExtractedEntity(Entity entity, float partialTicks) {}
}
