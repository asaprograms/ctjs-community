package com.chattriggers.ctjs.api.entity

import kotlin.test.Test
import kotlin.test.assertEquals

class TeamCompatibilityTest {
    @Test
    fun `legacy team visibility strings retain historical names`() {
        assertEquals("always", Team.Visibility.ALWAYS.legacyName)
        assertEquals("never", Team.Visibility.NEVER.legacyName)
        assertEquals("hideForOtherTeams", Team.Visibility.HIDE_FOR_OTHERS_TEAMS.legacyName)
        assertEquals("hideForOwnTeam", Team.Visibility.HIDE_FOR_OWN_TEAM.legacyName)
    }

    @Test
    fun `legacy team visibility accessors return strings`() {
        assertEquals(String::class.java, Team::class.java.getMethod("getNameTagVisibility").returnType)
        assertEquals(String::class.java, Team::class.java.getMethod("getDeathMessageVisibility").returnType)
        assertEquals(Team.Visibility::class.java, Team::class.java.getMethod("getNameTagVisibilityMode").returnType)
        assertEquals(Team.Visibility::class.java, Team::class.java.getMethod("getDeathMessageVisibilityMode").returnType)
    }
}
