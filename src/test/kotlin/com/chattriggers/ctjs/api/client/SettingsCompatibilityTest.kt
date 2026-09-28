package com.chattriggers.ctjs.api.client

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SettingsCompatibilityTest {
    @Test
    fun `legacy particle numbers retain their ordering`() {
        assertEquals(Settings.ParticlesMode.ALL, Settings.ParticlesMode.fromLegacy(0))
        assertEquals(Settings.ParticlesMode.DECREASED, Settings.ParticlesMode.fromLegacy(1))
        assertEquals(Settings.ParticlesMode.MINIMAL, Settings.ParticlesMode.fromLegacy(2))
        assertFailsWith<IllegalArgumentException> { Settings.ParticlesMode.fromLegacy(3) }
    }

    @Test
    fun `legacy chat visibility accepts historical names`() {
        assertEquals(Settings.ChatVisibility.HIDDEN, Settings.ChatVisibility.fromLegacy("hidden"))
        assertEquals(Settings.ChatVisibility.SYSTEM, Settings.ChatVisibility.fromLegacy("commands"))
        assertEquals(Settings.ChatVisibility.SYSTEM, Settings.ChatVisibility.fromLegacy("SYSTEM"))
        assertEquals(Settings.ChatVisibility.FULL, Settings.ChatVisibility.fromLegacy("full"))
        assertEquals(Settings.ChatVisibility.FULL, Settings.ChatVisibility.fromLegacy("anything-else"))
    }

    @Test
    fun `legacy settings getters retain primitive returns`() {
        assertEquals(Int::class.javaPrimitiveType, Settings::class.java.getMethod("getDifficulty").returnType)
        assertEquals(Int::class.javaPrimitiveType, Settings.VideoWrapper::class.java.getMethod("getClouds").returnType)
        assertEquals(Int::class.javaPrimitiveType, Settings.VideoWrapper::class.java.getMethod("getParticles").returnType)
        assertEquals(String::class.java, Settings.ChatWrapper::class.java.getMethod("getVisibility").returnType)
    }
}
