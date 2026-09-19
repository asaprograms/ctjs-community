package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.api.inventory.Item;
import com.chattriggers.ctjs.api.message.TextComponent;
import com.chattriggers.ctjs.api.triggers.TriggerType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {
    @Inject(method = "componentHoverEffect", at = @At("HEAD"), cancellable = true)
    private void ctjs$chatComponentHovered(Font font, Style style, int x, int y, CallbackInfo ci) {
        TriggerType.CHAT_COMPONENT_HOVERED.triggerAll(
            new TextComponent(Component.empty().withStyle(style)), x, y, ci
        );
    }

    @Inject(
        method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs$renderItemIntoGui(
        LivingEntity entity,
        Level level,
        ItemStack stack,
        int x,
        int y,
        int seed,
        CallbackInfo ci
    ) {
        TriggerType.RENDER_ITEM_INTO_GUI.triggerAll(Item.fromMC(stack), x, y, ci);
    }

    @Inject(
        method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs$renderItemOverlayIntoGui(
        Font font,
        ItemStack stack,
        int x,
        int y,
        String countText,
        CallbackInfo ci
    ) {
        TriggerType.RENDER_ITEM_OVERLAY_INTO_GUI.triggerAll(Item.fromMC(stack), x, y, ci);
    }
}
