package com.chattriggers.ctjs.api.render

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DisplayCompatibilityTest {
    @AfterTest
    fun clearDisplays() = DisplayHandler.clearDisplays()

    @Test
    fun `legacy displays register and retain visibility and location controls`() {
        val display = Display()

        assertEquals(1, DisplayHandler.registeredDisplayCount())
        display.setRenderLoc(12.75f, 34.5f).hide()
        assertEquals(12.75f, display.getRenderX())
        assertEquals(34.5f, display.getRenderY())
        assertFalse(display.getShouldRender())
        display.show()
        assertTrue(display.getShouldRender())
    }

    @Test
    fun `legacy display handler enums map onto modern display values`() {
        val display = Display()
            .setAlign(DisplayHandler.Align.RIGHT)
            .setBackground(DisplayHandler.Background.PER_LINE)
            .setOrder(DisplayHandler.Order.UP)
            .setRegisterType("post gui render")

        assertEquals(Text.Align.RIGHT, display.getAlign())
        assertEquals(Display.Background.PER_LINE, display.getBackground())
        assertEquals(Display.Order.REVERSED, display.getOrder())
        assertEquals(DisplayHandler.RegisterType.POST_GUI_RENDER, display.getRegisterType())
    }

    @Test
    fun `modern text retains the legacy display line text contract`() {
        val line = Text("hello").setText("legacy").setTextColor(0xff336699)

        assertEquals(line, line.getText())
        assertEquals("legacy", line.getString())
        assertEquals(0xff336699L, line.getTextColor())

        val display = Display().addLine("last").addLine(0, line)
        assertEquals(line, display.getLine(0))
    }

    @Test
    fun `legacy text width setter retains wrapping semantics`() {
        assertTrue(
            Text::class.java.methods.any {
                it.name == "setWidth" && it.parameterTypes.contentEquals(arrayOf(Int::class.javaPrimitiveType!!))
            },
            "Expected DisplayLine.setWidth(Int) to be exported",
        )
    }

    @Test
    fun `display line interaction methods remain exported`() {
        for (name in listOf(
            "registerClicked", "registerHovered", "registerMouseLeave", "registerDragged",
            "unregisterClicked", "unregisterHovered", "unregisterMouseLeave", "unregisterDragged",
        )) {
            assertTrue(Text::class.java.methods.any { it.name == name }, "Expected DisplayLine.$name")
        }
    }
}
