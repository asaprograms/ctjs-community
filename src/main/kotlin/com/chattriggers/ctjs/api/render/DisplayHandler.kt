package com.chattriggers.ctjs.api.render

import net.minecraft.client.gui.GuiGraphicsExtractor
import java.util.concurrent.CopyOnWriteArrayList

object DisplayHandler {
    private val displays = CopyOnWriteArrayList<Display>()
    private val currentContext = ThreadLocal<GuiGraphicsExtractor?>()

    internal fun registerDisplay(display: Display) {
        displays.addIfAbsent(display)
    }

    @JvmStatic
    fun clearDisplays() = displays.clear()

    internal fun renderOverlay(context: GuiGraphicsExtractor) = render(context, RegisterType.RENDER_OVERLAY)

    internal fun renderGui(context: GuiGraphicsExtractor) = render(context, RegisterType.POST_GUI_RENDER)

    internal fun context(): GuiGraphicsExtractor? = currentContext.get()

    internal fun registeredDisplayCount() = displays.size

    internal fun handleMouseClick(x: Double, y: Double, button: Int, pressed: Boolean) {
        displays.forEach { it.handleClick(x, y, button, pressed) }
    }

    internal fun handleMouseDrag(deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) {
        displays.forEach { it.handleDrag(deltaX, deltaY, x, y, button) }
    }

    private fun render(context: GuiGraphicsExtractor, type: RegisterType) {
        currentContext.set(context)
        try {
            displays.forEach { display ->
                if (display.getRegisterType() == type) display.render(context)
            }
        } finally {
            currentContext.remove()
        }
    }

    enum class RegisterType {
        RENDER_OVERLAY, POST_GUI_RENDER
    }

    enum class Background {
        NONE, FULL, PER_LINE
    }

    enum class Align {
        LEFT, CENTER, RIGHT
    }

    enum class Order {
        UP, DOWN
    }
}
