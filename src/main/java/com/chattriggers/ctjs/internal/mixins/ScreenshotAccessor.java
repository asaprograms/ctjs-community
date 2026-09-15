package com.chattriggers.ctjs.internal.mixins;

import net.minecraft.client.Screenshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.File;

@Mixin(Screenshot.class)
public interface ScreenshotAccessor {
    @Invoker("getFile")
    static File ctjs$getFile(File directory) {
        throw new AssertionError();
    }
}
