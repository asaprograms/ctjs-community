package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.api.triggers.TriggerType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void ctjs$guiDrawBackground(
        GuiGraphicsExtractor graphics,
        int mouseX,
        int mouseY,
        float partialTicks,
        CallbackInfo ci
    ) {
        TriggerType.GUI_DRAW_BACKGROUND.triggerAll(ci);
    }
}
