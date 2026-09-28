package com.chattriggers.ctjs.api

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConfigCompatibilityTest {
    @Test
    fun `legacy modules folder getter remains exported`() {
        val configClass = Class.forName("com.chattriggers.ctjs.api.Config", false, javaClass.classLoader)
        val getter = configClass.getMethod("getModulesFolder")

        assertEquals(String::class.java, getter.returnType)
        assertTrue(java.lang.reflect.Modifier.isStatic(getter.modifiers))
    }
}
