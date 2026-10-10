package com.chattriggers.ctjs.api.entity

import com.chattriggers.ctjs.MCBlockEntity

/**
 * Historical 1.8.9 name for a block entity wrapper.
 *
 * Modern Minecraft calls these block entities. This wrapper keeps the old
 * name and metadata accessor for modules that still use the original API.
 */
class TileEntity(mcValue: MCBlockEntity) : BlockEntity(mcValue) {
    /**
     * Returns the legacy metadata index of this entity's current block state.
     */
    fun getMetadata(): Int = getBlock().getMetadata()
}
