package com.chattriggers.ctjs.api

import com.chattriggers.ctjs.api.message.TextComponent
import com.chattriggers.ctjs.api.triggers.CancellableEvent
import com.chattriggers.ctjs.api.triggers.ChatTrigger

/** Compatibility helpers for the non-Forge portions of ChatTriggers 1.8.9 EventLib. */
object EventLib {
    /** Cancels a modern CTJS cancellable event. */
    @JvmStatic
    fun cancel(event: Any) {
        (event as? CancellableEvent)?.setCanceled()
            ?: throw IllegalArgumentException("Event is not cancellable by CTJS")
    }

    /** Returns the full chat component from a chat-trigger event. */
    @JvmStatic
    fun getMessage(event: ChatTrigger.Event): TextComponent = event.message

    /** Minecraft 26.x no longer carries the old Forge chat-type byte. */
    @JvmStatic
    fun getType(event: ChatTrigger.Event): Int = 0
}
