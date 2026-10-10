package com.chattriggers.ctjs.api.render

import com.chattriggers.ctjs.api.triggers.RegularTrigger
import net.minecraft.client.gui.screens.Screen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GuiCompatibilityTest {
    @Test
    fun `legacy gui callback registrations return triggers`() {
        for (methodName in listOf(
            "registerDraw", "registerClicked", "registerScrolled", "registerKeyTyped",
            "registerMouseDragged", "registerMouseReleased", "registerActionPerformed",
        )) {
            val method = Gui::class.java.getMethod(methodName, Any::class.java)
            assertEquals(RegularTrigger::class.java, method.returnType, "$methodName must return a trigger")
        }
    }

    @Test
    fun `legacy gui handler surface remains available`() {
        GuiHandler::class.java.getMethod("openGui", Screen::class.java)
        GuiHandler::class.java.getMethod("clearGuis")
    }

    @Test
    fun `legacy explicit button ID overload remains available`() {
        val expectedParameters = arrayOf(
            Int::class.javaPrimitiveType!!,
            Int::class.javaPrimitiveType!!,
            Int::class.javaPrimitiveType!!,
            Int::class.javaPrimitiveType!!,
            Int::class.javaPrimitiveType!!,
            String::class.java,
        )
        val method = Gui::class.java.getMethod("addButton", *expectedParameters)

        assertEquals(Gui::class.java, method.returnType)
        assertTrue(method.parameterTypes.contentEquals(expectedParameters))
    }

    @Test
    fun `legacy tooltip setter remains chainable`() {
        assertEquals(Gui::class.java, Gui::class.java.getMethod("setTooltip", String::class.java).returnType)
    }
}
