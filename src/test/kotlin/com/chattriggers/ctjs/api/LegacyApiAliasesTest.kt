package com.chattriggers.ctjs.api

import com.chattriggers.ctjs.api.client.Client
import com.chattriggers.ctjs.api.entity.LivingEntity
import com.chattriggers.ctjs.api.inventory.Inventory
import com.chattriggers.ctjs.api.inventory.Item
import com.chattriggers.ctjs.api.world.Chunk
import com.chattriggers.ctjs.api.world.Scoreboard
import com.chattriggers.ctjs.api.world.TabList
import com.chattriggers.ctjs.api.world.World
import kotlin.test.Test
import kotlin.test.assertTrue

class LegacyApiAliasesTest {
    @Test
    fun `common 1_8_9 method names remain exported`() {
        assertMethod(Client::class.java, "getChatGUI")
        assertMethod(LivingEntity::class.java, "getItemInSlot")
        assertMethod(Inventory::class.java, "getItemInSlot")
        assertMethod(Item::class.java, "isDamagable")
        assertMethod(Scoreboard::class.java, "getScoreboardTitle")
        assertMethod(TabList::class.java, "getFooterMessage")
        assertMethod(World::class.java, "getAllTileEntities")
        assertMethod(World::class.java, "getAllTileEntitiesOfType")
        assertMethod(Chunk::class.java, "getAllTileEntities")
        assertMethod(Chunk::class.java, "getAllTileEntitiesOfType")
    }

    private fun assertMethod(type: Class<*>, name: String) {
        assertTrue(type.methods.any { it.name == name }, "Expected ${type.simpleName}.$name to be exported")
    }
}
