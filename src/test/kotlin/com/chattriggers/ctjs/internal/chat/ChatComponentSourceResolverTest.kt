package com.chattriggers.ctjs.internal.chat

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ChatComponentSourceResolverTest {
    @Test
    fun `finds the styled source component within a chat message`() {
        val selectedStyle = Style.EMPTY.withColor(ChatFormatting.GREEN)
        val message = Component.literal("before ")
            .append(Component.literal("selected").withStyle(selectedStyle))
            .append(Component.literal(" after"))

        val match = ChatComponentSourceResolver.findMatchingComponent(message, selectedStyle)

        assertEquals("selected", match?.string)
        assertEquals(selectedStyle, match?.style)
    }

    @Test
    fun `does not select unrelated styled text`() {
        val message = Component.literal("plain")
        val selectedStyle = Style.EMPTY.withColor(ChatFormatting.RED)

        assertNull(ChatComponentSourceResolver.findMatchingComponent(message, selectedStyle))
    }
}
