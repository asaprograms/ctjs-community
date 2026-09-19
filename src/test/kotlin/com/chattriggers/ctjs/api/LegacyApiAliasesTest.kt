package com.chattriggers.ctjs.api

import com.chattriggers.ctjs.api.client.Client
import com.chattriggers.ctjs.api.client.Settings
import com.chattriggers.ctjs.api.entity.LivingEntity
import com.chattriggers.ctjs.api.entity.Entity
import com.chattriggers.ctjs.api.inventory.Inventory
import com.chattriggers.ctjs.api.inventory.Item
import com.chattriggers.ctjs.api.message.ChatLib
import com.chattriggers.ctjs.api.world.Chunk
import com.chattriggers.ctjs.api.world.Scoreboard
import com.chattriggers.ctjs.api.world.TabList
import com.chattriggers.ctjs.api.world.World
import com.chattriggers.ctjs.api.world.block.BlockFace
import com.chattriggers.ctjs.engine.Register
import kotlin.test.Test
import kotlin.test.assertTrue
import java.lang.reflect.Modifier

class LegacyApiAliasesTest {
    @Test
    fun `common 1_8_9 method names remain exported`() {
        assertStaticMethod(Client::class.java, "getChatGUI")
        assertStaticMethod(ChatLib::class.java, "isPlayer")
        for (part in listOf("Cape", "Jacket", "LeftSleeve", "RightSleeve", "LeftPantsLeg", "RightPantsLeg", "Hat")) {
            assertMethod(Settings.SkinWrapper::class.java, "get$part")
            assertMethod(Settings.SkinWrapper::class.java, "set$part")
        }
        assertStaticMethod(com.chattriggers.ctjs.api.client.Player::class.java, "getRawYaw")
        assertStaticMethod(com.chattriggers.ctjs.api.client.Player::class.java, "getUUIDObj")
        assertStaticMethod(com.chattriggers.ctjs.api.client.Player::class.java, "getOpenedInventory")
        assertMethod(Entity::class.java, "getRider")
        assertMethod(LivingEntity::class.java, "getItemInSlot")
        assertMethod(Inventory::class.java, "getItemInSlot")
        assertMethod(Item::class.java, "isDamagable")
        for (name in listOf("getID", "getMetadata", "getRegistryName", "getUnlocalizedName", "getTextComponent", "setDamage")) {
            assertMethod(Item::class.java, name)
        }
        assertMethod(Scoreboard.Score::class.java, "getPoints")
        assertMethod(Scoreboard.Score::class.java, "setPoints")
        assertStaticMethod(BlockFace::class.java, "fromMCEnumFacing")
        assertMethod(BlockFace::class.java, "getName")
        assertStaticMethod(Scoreboard::class.java, "getScoreboardTitle")
        assertStaticMethod(TabList::class.java, "getFooterMessage")
        assertStaticMethod(TabList::class.java, "getHeaderMessage")
        assertStaticMethod(World::class.java, "getAllTileEntities")
        assertStaticMethod(World::class.java, "getAllTileEntitiesOfType")
        assertMethod(Chunk::class.java, "getAllTileEntities")
        assertMethod(Chunk::class.java, "getAllTileEntitiesOfType")
        assertStaticMethod(Register::class.java, "registerRenderSlot")
        assertStaticMethod(Register::class.java, "registerGuiMouseRelease")
        assertStaticMethod(Register::class.java, "registerAttackEntity")
        assertStaticMethod(Register::class.java, "registerHitBlock")
        assertStaticMethod(Register::class.java, "registerBlockBreak")
        assertStaticMethod(Register::class.java, "registerScreenshotTaken")
        assertStaticMethod(Register::class.java, "registerRenderTileEntity")
        assertStaticMethod(Register::class.java, "registerPostRenderEntity")
        assertStaticMethod(Register::class.java, "registerPostRenderTileEntity")
        assertStaticMethod(Register::class.java, "registerRenderSlotHighlight")
        assertStaticMethod(Register::class.java, "registerPreItemRender")
        assertStaticMethod(Register::class.java, "registerRenderItemIntoGui")
        assertStaticMethod(Register::class.java, "registerRenderItemOverlayIntoGui")
        assertStaticMethod(Register::class.java, "registerChatComponentClicked")
        assertStaticMethod(Register::class.java, "registerChatComponentHovered")
        assertStaticMethod(Register::class.java, "registerPickupItem")
        assertStaticMethod(Register::class.java, "registerNoteBlockPlay")
        assertStaticMethod(Register::class.java, "registerNoteBlockChange")
        for (name in listOf("Crosshair", "Debug", "BossHealth", "Health", "Armor", "Food", "MountHealth", "Hotbar", "Air", "Portal", "Chat", "Scoreboard", "Title")) {
            assertStaticMethod(Register::class.java, "registerRender$name")
        }
        assertStaticMethod(Register::class.java, "registerGuiDrawBackground")
        assertStaticMethod(Register::class.java, "registerRenderHand")
        assertStaticMethod(Register::class.java, "registerRenderHelmet")
        assertStaticMethod(Register::class.java, "registerRenderExperience")
        assertStaticMethod(Register::class.java, "registerRenderJumpBar")
        assertStaticMethod(Register::class.java, "registerPlayerJoined")
        assertStaticMethod(Register::class.java, "registerPlayerLeft")
    }

    private fun assertMethod(type: Class<*>, name: String) {
        assertTrue(type.methods.any { it.name == name }, "Expected ${type.simpleName}.$name to be exported")
    }

    private fun assertStaticMethod(type: Class<*>, name: String) {
        assertTrue(
            type.methods.any { it.name == name && Modifier.isStatic(it.modifiers) },
            "Expected static ${type.simpleName}.$name to be exported",
        )
    }
}
