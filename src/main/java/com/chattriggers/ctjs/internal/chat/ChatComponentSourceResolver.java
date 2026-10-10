package com.chattriggers.ctjs.internal.chat;

import com.chattriggers.ctjs.internal.mixins.ChatComponentAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

/**
 * Resolves the source component behind a style selected from the modern chat
 * renderer. Modern click and hover callbacks only provide the resolved style,
 * but chat history still retains the original component tree.
 */
public final class ChatComponentSourceResolver {
    private ChatComponentSourceResolver() {}

    public static Component resolve(Style style) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gui == null) {
            return Component.empty().withStyle(style);
        }

        for (GuiMessage message : ((ChatComponentAccessor) minecraft.gui.getChat()).getAllMessages()) {
            Component match = findMatchingComponent(message.content(), style);
            if (match != null) {
                return match.copy().withStyle(style);
            }
        }

        return Component.empty().withStyle(style);
    }

    public static Component findMatchingComponent(Component component, Style style) {
        if (component.getStyle().equals(style)) {
            return component;
        }

        for (Component sibling : component.getSiblings()) {
            Component match = findMatchingComponent(sibling, style);
            if (match != null) {
                return match;
            }
        }

        return null;
    }
}
