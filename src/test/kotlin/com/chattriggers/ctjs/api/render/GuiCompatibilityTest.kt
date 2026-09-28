package com.chattriggers.ctjs.api.render

import com.chattriggers.ctjs.api.triggers.RegularTrigger
import net.minecraft.client.gui.screens.Screen
import kotlin.test.Test
import kotlin.test.assertEquals

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
}
