package com.chattriggers.ctjs.api.entity

import com.chattriggers.ctjs.internal.mixins.PlayerInfoAccessor
import net.minecraft.network.chat.Component
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerMPCompatibilityTest {
    @Test
    fun `player info accessor exposes tab display name mutation`() {
        val method = PlayerInfoAccessor::class.java.getMethod(
            "ctjs_setTabListDisplayName",
            Component::class.java,
        )

        assertEquals(Void.TYPE, method.returnType)
    }
}
