package com.chattriggers.ctjs.api.world

import com.chattriggers.ctjs.api.CTWrapper
import com.chattriggers.ctjs.api.entity.BlockEntity
import com.chattriggers.ctjs.api.entity.Entity
import com.chattriggers.ctjs.internal.mixins.ChunkAccessAccessor
import com.chattriggers.ctjs.MCBlockPos
import com.chattriggers.ctjs.MCChunk
import com.chattriggers.ctjs.MCEntity
import com.chattriggers.ctjs.internal.utils.asMixin
import net.minecraft.world.phys.AABB
import net.minecraft.world.level.LightLayer

// TODO: Add more methods here?
class Chunk(override val mcValue: MCChunk) : CTWrapper<MCChunk> {
    /**
     * Gets the x position of the chunk
     */
    fun getX() = mcValue.pos.x

    /**
     * Gets the z position of the chunk
     */
    fun getZ() = mcValue.pos.z

    /**
     * Gets the minimum x coordinate of a block in the chunk
     *
     * @return the minimum x coordinate
     */
    fun getMinBlockX() = getX() * 16

    /**
     * Gets the minimum z coordinate of a block in the chunk
     *
     * @return the minimum z coordinate
     */
    fun getMinBlockZ() = getZ() * 16

    /** Gets the sky light level at an absolute block position in this chunk. */
    fun getSkyLightLevel(x: Int, y: Int, z: Int): Int {
        return World.toMC()?.getBrightness(LightLayer.SKY, MCBlockPos(x, y, z)) ?: 0
    }

    /** Gets the block light level at an absolute block position in this chunk. */
    fun getBlockLightLevel(x: Int, y: Int, z: Int): Int {
        return World.toMC()?.getBrightness(LightLayer.BLOCK, MCBlockPos(x, y, z)) ?: 0
    }

    /**
     * Gets every entity in this chunk
     *
     * @return the entity list
     */
    fun getAllEntities(): List<Entity> = getAllEntitiesOfType(MCEntity::class.java)

    /**
     * Gets every entity in this chunk of a certain class
     *
     * @param clazz the class to filter for (Use `Java.type().class` to get this)
     * @return the entity list
     */
    fun getAllEntitiesOfType(clazz: Class<MCEntity>): List<Entity> {
        val box = AABB(
            MCBlockPos(getMinBlockX(), mcValue.minY, getMinBlockZ())
        ).expandTowards(16.0, mcValue.maxY.toDouble(), 16.0)

        return World.toMC()?.getEntitiesOfClass(clazz, box) { true }?.map(Entity::fromMC) ?: listOf()
    }

    /**
     * Gets every block entity in this chunk
     *
     * @return the block entity list
     */
    fun getAllBlockEntities(): List<BlockEntity> {
        return mcValue.asMixin<ChunkAccessAccessor>().blockEntities.values.map(::BlockEntity)
    }

    /** Legacy 1.8.9 name for [getAllBlockEntities]. */
    fun getAllTileEntities() = getAllBlockEntities()

    /**
     * Gets every block entity in this chunk of a certain class
     *
     * @param clazz the class to filter for (Use `Java.type().class` to get this)
     * @return the block entity list
     */
    fun getAllBlockEntitiesOfType(clazz: Class<*>): List<BlockEntity> {
        return getAllBlockEntities().filter {
            clazz.isInstance(it.toMC())
        }
    }

    /** Legacy 1.8.9 name for [getAllBlockEntitiesOfType]. */
    fun getAllTileEntitiesOfType(clazz: Class<*>) = getAllBlockEntitiesOfType(clazz)
}
