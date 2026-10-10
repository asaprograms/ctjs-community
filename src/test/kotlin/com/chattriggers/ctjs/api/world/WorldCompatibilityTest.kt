package com.chattriggers.ctjs.api.world

import kotlin.test.Test
import kotlin.test.assertEquals

class WorldCompatibilityTest {
    @Test
    fun `legacy difficulty accessor returns a string`() {
        assertEquals(String::class.java, World::class.java.getMethod("getDifficulty").returnType)
        assertEquals(com.chattriggers.ctjs.api.client.Settings.Difficulty::class.java, World::class.java.getMethod("getDifficultyMode").returnType)
    }

    @Test
    fun `moon phase follows the eight day cycle`() {
        assertEquals(0, World.moonPhaseAt(0))
        assertEquals(1, World.moonPhaseAt(24_000))
        assertEquals(7, World.moonPhaseAt(7 * 24_000L))
        assertEquals(0, World.moonPhaseAt(8 * 24_000L))
        assertEquals(7, World.moonPhaseAt(-1))
    }

    @Test
    fun `legacy border size retains its integer return`() {
        assertEquals(Int::class.javaPrimitiveType, World.BorderWrapper::class.java.getMethod("getSize").returnType)
        assertEquals(Double::class.javaPrimitiveType, World.BorderWrapper::class.java.getMethod("getSizeExact").returnType)
    }
}
