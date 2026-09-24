package com.chattriggers.ctjs.internal.commands

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MigrationCompatibilityTest {
    @Test
    fun `legacy getRider is no longer reported as unavailable`() {
        val collectErrors = Migration::class.java.getDeclaredMethod("collectErrors", String::class.java)
        collectErrors.isAccessible = true

        @Suppress("UNCHECKED_CAST")
        val errors = collectErrors.invoke(Migration, "entity.getRider()") as List<Pair<Int, String>>

        assertFalse(errors.any { it.second.contains("getRider") })
        assertTrue(errors.none { it.second.contains("Error") })
    }
}
