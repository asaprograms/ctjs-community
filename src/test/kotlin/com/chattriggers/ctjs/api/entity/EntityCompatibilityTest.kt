package com.chattriggers.ctjs.api.entity

import net.minecraft.world.phys.Vec3
import kotlin.test.Test
import kotlin.test.assertEquals

class EntityCompatibilityTest {
    @Test
    fun `base entity retains legacy health accessors`() {
        assertEquals(Float::class.javaPrimitiveType, Entity::class.java.getMethod("getHP").returnType)
        assertEquals(Float::class.javaPrimitiveType, Entity::class.java.getMethod("getMaxHP").returnType)
    }

    @Test
    fun `legacy eye position interpolation overload remains exported`() {
        val method = Entity::class.java.getMethod("getEyePosition", Float::class.javaPrimitiveType)
        assertEquals(Vec3::class.java, method.returnType)
    }
}
