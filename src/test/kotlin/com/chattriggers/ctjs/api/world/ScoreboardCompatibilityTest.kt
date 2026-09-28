package com.chattriggers.ctjs.api.world

import com.chattriggers.ctjs.api.message.TextComponent
import kotlin.test.Test
import kotlin.test.assertEquals

class ScoreboardCompatibilityTest {
    @Test
    fun `legacy scoreboard text accessors return strings`() {
        assertEquals(String::class.java, Scoreboard::class.java.getMethod("getTitle").returnType)
        assertEquals(String::class.java, Scoreboard::class.java.getMethod("getScoreboardTitle").returnType)
        assertEquals(String::class.java, Scoreboard.Score::class.java.getMethod("getName").returnType)
    }

    @Test
    fun `component accessors remain explicit`() {
        assertEquals(TextComponent::class.java, Scoreboard::class.java.getMethod("getTitleComponent").returnType)
        assertEquals(TextComponent::class.java, Scoreboard.Score::class.java.getMethod("getNameComponent").returnType)
    }
}
