package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.api.message.TextComponent;
import com.chattriggers.ctjs.api.triggers.CancellableEvent;
import com.chattriggers.ctjs.api.triggers.TriggerType;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(method = "handleComponentClicked", at = @At("HEAD"), cancellable = true)
    private void ctjs$chatComponentClicked(
        Style style,
        boolean insertionClickMode,
        CallbackInfoReturnable<Boolean> cir
    ) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.CHAT_COMPONENT_CLICKED.triggerAll(
            new TextComponent(Component.empty().withStyle(style)), event
        );
        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }
}
