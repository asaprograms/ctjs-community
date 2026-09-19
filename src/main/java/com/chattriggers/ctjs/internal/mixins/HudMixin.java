package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.api.world.Scoreboard;
import com.chattriggers.ctjs.api.message.TextComponent;
import com.chattriggers.ctjs.api.triggers.TriggerType;
import com.chattriggers.ctjs.internal.engine.CTEvents;
import gg.essential.universal.UMatrixStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Gui;
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

@Mixin(Gui.class)
public abstract class HudMixin {
    @Shadow private Component title;
    @Shadow private Component subtitle;
    @Shadow protected abstract void extractTextureOverlay(GuiGraphicsExtractor graphics, Identifier texture, float alpha);

    @Redirect(
        method = "extractCameraOverlays",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;extractTextureOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/resources/Identifier;F)V",
            ordinal = 0
        )
    )
    private void ctjs$renderHelmet(Gui instance, GuiGraphicsExtractor graphics, Identifier texture, float alpha) {
        com.chattriggers.ctjs.api.triggers.CancellableEvent event =
            new com.chattriggers.ctjs.api.triggers.CancellableEvent();
        TriggerType.RENDER_HELMET.triggerAll(event);
        if (!event.isCancelled()) {
            extractTextureOverlay(graphics, texture, alpha);
        }
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
