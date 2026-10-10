package com.chattriggers.ctjs.api.entity

import com.chattriggers.ctjs.api.world.Chunk
import com.chattriggers.ctjs.api.world.World
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TileEntityCompatibilityTest {
    @Test
    fun `legacy tile entity wrapper preserves its public surface`() {
        assertTrue(BlockEntity::class.java.isAssignableFrom(TileEntity::class.java))
        assertEquals(Int::class.javaPrimitiveType, TileEntity::class.java.getMethod("getMetadata").returnType)
    }

    @Test
    fun `legacy tile entity list aliases expose tile entity wrappers`() {
        assertTrue(
            World::class.java.getMethod("getAllTileEntities").genericReturnType.typeName.contains(TileEntity::class.java.name),
        )
        assertTrue(
            Chunk::class.java.getMethod("getAllTileEntities").genericReturnType.typeName.contains(TileEntity::class.java.name),
        )
    }
}
