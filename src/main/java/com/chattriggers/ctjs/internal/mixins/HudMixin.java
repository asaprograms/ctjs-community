package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.api.world.Scoreboard;
import com.chattriggers.ctjs.api.message.TextComponent;
import com.chattriggers.ctjs.api.triggers.TriggerType;
import com.chattriggers.ctjs.api.triggers.CancellableEvent;
import com.chattriggers.ctjs.internal.engine.CTEvents;
import gg.essential.universal.UMatrixStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.contextualbar.ContextualBar;
import net.minecraft.client.gui.contextualbar.ExperienceBar;
import net.minecraft.client.gui.contextualbar.JumpableVehicleBar;
import net.minecraft.world.scores.Objective;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class HudMixin {
    private boolean ctjs$cancelExperienceBar;
    private boolean ctjs$cancelJumpBar;
    @Shadow private Component title;
    @Shadow private Component subtitle;
    @Shadow protected abstract void extractTextureOverlay(GuiGraphicsExtractor graphics, Identifier texture, float alpha);

    @Redirect(
        method = "extractCameraOverlays",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Hud;extractTextureOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/resources/Identifier;F)V",
            ordinal = 0
        )
    )
    private void ctjs$renderHelmet(Hud instance, GuiGraphicsExtractor graphics, Identifier texture, float alpha) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.RENDER_HELMET.triggerAll(event);
        if (!event.isCancelled()) {
            extractTextureOverlay(graphics, texture, alpha);
        }
    }

    @Redirect(
        method = "extractHotbarAndDecorations",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/contextualbar/ContextualBar;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
        )
    )
    private void ctjs$renderContextualBarBackground(ContextualBar bar, GuiGraphicsExtractor graphics, DeltaTracker delta) {
        ctjs$cancelExperienceBar = false;
        ctjs$cancelJumpBar = false;

        if (bar instanceof ExperienceBar) {
            CancellableEvent event = new CancellableEvent();
            TriggerType.RENDER_EXPERIENCE.triggerAll(event);
            ctjs$cancelExperienceBar = event.isCancelled();
        } else if (bar instanceof JumpableVehicleBar) {
            CancellableEvent event = new CancellableEvent();
            TriggerType.RENDER_JUMP_BAR.triggerAll(event);
            ctjs$cancelJumpBar = event.isCancelled();
        }

        if (!ctjs$cancelExperienceBar && !ctjs$cancelJumpBar) {
            bar.extractBackground(graphics, delta);
        }
    }

    @Redirect(
        method = "extractHotbarAndDecorations",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/contextualbar/ContextualBar;extractExperienceLevel(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;I)V"
        )
    )
    private void ctjs$renderExperienceLevel(GuiGraphicsExtractor graphics, Font font, int level) {
        if (!ctjs$cancelExperienceBar) {
            ContextualBar.extractExperienceLevel(graphics, font, level);
        }
    }

    @Redirect(
        method = "extractHotbarAndDecorations",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/contextualbar/ContextualBar;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
        )
    )
    private void ctjs$renderContextualBarState(ContextualBar bar, GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (!ctjs$cancelExperienceBar && !ctjs$cancelJumpBar) {
            bar.extractRenderState(graphics, delta);
        }
        ctjs$cancelExperienceBar = false;
        ctjs$cancelJumpBar = false;
    }

    @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderCrosshair(GuiGraphicsExtractor graphics, DeltaTracker delta, CallbackInfo ci) {
        TriggerType.RENDER_CROSSHAIR.triggerAll(ci);
    }

    @Inject(method = "extractDebugOverlay", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderDebug(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        TriggerType.RENDER_DEBUG.triggerAll(ci);
    }

    @Inject(method = "extractBossOverlay", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderBossHealth(GuiGraphicsExtractor graphics, DeltaTracker delta, CallbackInfo ci) {
        TriggerType.RENDER_BOSS_HEALTH.triggerAll(ci);
    }

    @Inject(method = "extractPlayerHealth", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderHealth(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        TriggerType.RENDER_HEALTH.triggerAll(ci);
    }

    @Inject(method = "extractArmor", at = @At("HEAD"), cancellable = true)
    private static void ctjs$renderArmor(GuiGraphicsExtractor graphics, Player player, int y, int lines, int height, int x, CallbackInfo ci) {
        TriggerType.RENDER_ARMOR.triggerAll(ci);
    }

    @Inject(method = "extractFood", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderFood(GuiGraphicsExtractor graphics, Player player, int top, int right, CallbackInfo ci) {
        TriggerType.RENDER_FOOD.triggerAll(ci);
    }

    @Inject(method = "extractVehicleHealth", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderMountHealth(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        TriggerType.RENDER_MOUNT_HEALTH.triggerAll(ci);
    }

    @Inject(method = "extractItemHotbar", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderHotbar(GuiGraphicsExtractor graphics, DeltaTracker delta, CallbackInfo ci) {
        TriggerType.RENDER_HOTBAR.triggerAll(ci);
    }

    @Inject(method = "extractAirBubbles", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderAir(GuiGraphicsExtractor graphics, Player player, int top, int right, int lines, CallbackInfo ci) {
        TriggerType.RENDER_AIR.triggerAll(ci);
    }

    @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderPortal(GuiGraphicsExtractor graphics, float alpha, CallbackInfo ci) {
        TriggerType.RENDER_PORTAL.triggerAll(ci);
    }

    @Inject(method = "extractChat", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderChat(GuiGraphicsExtractor graphics, DeltaTracker delta, CallbackInfo ci) {
        TriggerType.RENDER_CHAT.triggerAll(ci);
    }

    @Inject(method = "extractTitle", at = @At("HEAD"), cancellable = true)
    private void ctjs$renderTitle(GuiGraphicsExtractor graphics, DeltaTracker delta, CallbackInfo ci) {
        TriggerType.RENDER_TITLE.triggerAll(
            title == null ? null : new TextComponent(title),
            subtitle == null ? null : new TextComponent(subtitle),
            ci
        );
    }

    @Inject(method = "displayScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void injectRenderScoreboard(GuiGraphicsExtractor graphics, Objective objective, CallbackInfo ci) {
        TriggerType.RENDER_SCOREBOARD.triggerAll(ci);
        if (!Scoreboard.getShouldRender())
            ci.cancel();
    }

    @Inject(
        method = "extractBossOverlay",
        at = @At("TAIL")
    )
    private void injectRenderOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        CTEvents.RENDER_OVERLAY.invoker().render(graphics, new UMatrixStack(graphics.pose()).toMC(), deltaTracker.getGameTimeDeltaTicks());
    }
}
