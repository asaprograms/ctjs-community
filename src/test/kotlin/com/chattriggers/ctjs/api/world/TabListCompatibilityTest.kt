package com.chattriggers.ctjs.api.world

import com.chattriggers.ctjs.api.message.Message
import com.chattriggers.ctjs.api.message.TextComponent
import kotlin.test.Test
import kotlin.test.assertEquals

class TabListCompatibilityTest {
    @Test
    fun `legacy tab message accessors return message wrappers`() {
        assertEquals(Message::class.java, TabList::class.java.getMethod("getHeaderMessage").returnType)
        assertEquals(Message::class.java, TabList::class.java.getMethod("getFooterMessage").returnType)
    }

    @Test
    fun `component accessors remain explicit`() {
        assertEquals(TextComponent::class.java, TabList::class.java.getMethod("getHeaderComponent").returnType)
        assertEquals(TextComponent::class.java, TabList::class.java.getMethod("getFooterComponent").returnType)
    }
}
