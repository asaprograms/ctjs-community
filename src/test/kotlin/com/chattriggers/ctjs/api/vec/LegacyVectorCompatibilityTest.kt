package com.chattriggers.ctjs.api.vec

import com.chattriggers.ctjs.api.world.block.BlockPos
import kotlin.test.Test
import kotlin.test.assertEquals

class LegacyVectorCompatibilityTest {
    @Test
    fun `legacy vector distances and ordering retain their contracts`() {
        val origin = Vec3i(0, 0, 0)
        val point = Vec3i(3, 4, 12)

        assertEquals(169.0, origin.distanceSq(point))
        assertEquals(13.0, origin.distance(point))
        assertEquals(0.0, origin.distanceSqToCenter(0.5, 0.5, 0.5))
        assertEquals(listOf(Vec3i(2, 0, 0), Vec3i(0, 0, 1), Vec3i(0, 1, 0)), listOf(Vec3i(0, 1, 0), Vec3i(0, 0, 1), Vec3i(2, 0, 0)).sorted())
    }

    @Test
    fun `legacy block position arithmetic returns block positions`() {
        val pos = BlockPos(10, 20, 30)

        assertEquals(BlockPos(11, 22, 33), pos.add(1, 2, 3))
        assertEquals(BlockPos(9, 18, 27), pos.subtract(Vec3i(1, 2, 3)))
        assertEquals(pos.mcValue, pos.toMCBlock())
    }
}
