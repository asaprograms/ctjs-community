package com.chattriggers.ctjs.api.world

import com.chattriggers.ctjs.api.message.Message
import com.chattriggers.ctjs.api.message.TextComponent
import kotlin.test.Test
import kotlin.test.assertEquals
import java.lang.reflect.ParameterizedType

class TabListCompatibilityTest {
    @Test
    fun `legacy tab names retain their string element type`() {
        val names = TabList::class.java.getMethod("getNames").genericReturnType as ParameterizedType
        val list = TabList::class.java.getMethod("getList").genericReturnType as ParameterizedType

        assertEquals(String::class.java, names.actualTypeArguments.single())
        assertEquals(TabList.Name::class.java, list.actualTypeArguments.single())
    }

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
