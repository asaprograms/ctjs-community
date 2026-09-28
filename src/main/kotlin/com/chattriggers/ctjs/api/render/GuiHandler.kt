package com.chattriggers.ctjs.api.render

import com.chattriggers.ctjs.api.client.Client
import net.minecraft.client.gui.screens.Screen

/**
 * Legacy GUI scheduling bridge.
 *
 * ChatTriggers 1.8.9 deferred GUI opening until a client tick. The modern
 * client GUI setter already schedules that transition, so this wrapper keeps
 * the old entry point without maintaining a second queue.
 */
object GuiHandler {
    @JvmStatic
    fun openGui(gui: Screen) {
        Client.currentGui.set(gui)
    }

    /** Kept for modules that cleared the former pending-open queue. */
    @JvmStatic
    fun clearGuis() = Unit
}
