package com.chattriggers.ctjs.api.entity

import kotlin.test.Test
import kotlin.test.assertEquals

class EntityCompatibilityTest {
    @Test
    fun `base entity retains legacy health accessors`() {
        assertEquals(Float::class.javaPrimitiveType, Entity::class.java.getMethod("getHP").returnType)
        assertEquals(Float::class.javaPrimitiveType, Entity::class.java.getMethod("getMaxHP").returnType)
    }
}
