package com.chattriggers.ctjs.api.world.block

import net.minecraft.world.level.block.Blocks
import net.minecraft.server.Bootstrap
import net.minecraft.SharedConstants
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class BlockCompatibilityTest {
    @BeforeTest
    fun bootstrapRegistries() {
        SharedConstants.tryDetectVersion()
        Bootstrap.bootStrap()
    }

    @Test
    fun `legacy block setters mutate and return the same wrapper`() {
        val block = Block(BlockType(Blocks.STONE))
        val position = BlockPos(4, 5, 6)

        assertSame(block, block.setBlockPos(position))
        assertSame(block, block.setFace(BlockFace.NORTH))
        assertEquals(position, block.blockPos)
        assertEquals(BlockFace.NORTH, block.face)
    }

    @Test
    fun `legacy type methods delegate through block wrapper`() {
        val block = Block(BlockType(Blocks.STONE))

        assertSame(Blocks.STONE, block.block)
        assertEquals("minecraft:stone", block.getRegistryName())
        assertEquals("block.minecraft.stone", block.getUnlocalizedName())
        assertEquals(block.type.getDefaultMetadata(), block.getDefaultMetadata())
    }

    @Test
    fun `legacy block face offset names remain available`() {
        assertEquals(-1, BlockFace.WEST.getXOffset())
        assertEquals(1, BlockFace.UP.getYOffset())
        assertEquals(1, BlockFace.SOUTH.getZOffset())
        assertEquals("x", BlockFace.Axis.X.getName())
    }
}
