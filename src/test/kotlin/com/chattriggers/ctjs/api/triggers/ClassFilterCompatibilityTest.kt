package com.chattriggers.ctjs.api.triggers

import kotlin.test.Test
import kotlin.test.assertTrue

class ClassFilterCompatibilityTest {
    @Test
    fun `legacy packet filter aliases remain exported`() {
        val names = ClassFilterTrigger::class.java.methods.mapTo(mutableSetOf()) { it.name }

        assertTrue("setPacketClass" in names)
        assertTrue("setPacketClasses" in names)
    }
}
