package com.chattriggers.ctjs.api.world

import kotlin.test.Test
import kotlin.test.assertEquals

class WorldCompatibilityTest {
    @Test
    fun `moon phase follows the eight day cycle`() {
        assertEquals(0, World.moonPhaseAt(0))
        assertEquals(1, World.moonPhaseAt(24_000))
        assertEquals(7, World.moonPhaseAt(7 * 24_000L))
        assertEquals(0, World.moonPhaseAt(8 * 24_000L))
        assertEquals(7, World.moonPhaseAt(-1))
    }
}
