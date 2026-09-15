package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.api.triggers.CancellableEvent;
import com.chattriggers.ctjs.api.triggers.TriggerType;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

@Mixin(Screenshot.class)
public class ScreenshotMixin {
    @Inject(
        method = "grab(Ljava/io/File;Ljava/lang/String;Lcom/mojang/blaze3d/pipeline/RenderTarget;ILjava/util/function/Consumer;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void ctjs$onScreenshot(
        File gameDirectory,
        String requestedName,
        RenderTarget renderTarget,
        int downscale,
        Consumer<Component> messageConsumer,
        CallbackInfo ci
    ) {
        File screenshotDirectory = new File(gameDirectory, Screenshot.SCREENSHOT_DIR);
        String fileName = requestedName != null
            ? requestedName
            : ScreenshotAccessor.ctjs$getFile(screenshotDirectory).getName();
        CancellableEvent event = new CancellableEvent();

        TriggerType.SCREENSHOT_TAKEN.triggerAll(fileName, event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
