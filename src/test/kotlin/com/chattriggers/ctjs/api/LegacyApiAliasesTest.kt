package com.chattriggers.ctjs.api

import com.chattriggers.ctjs.api.client.Client
import com.chattriggers.ctjs.api.client.Settings
import com.chattriggers.ctjs.api.client.Sound
import com.chattriggers.ctjs.api.client.KeyBind
import com.chattriggers.ctjs.api.entity.LivingEntity
import com.chattriggers.ctjs.api.entity.Entity
import com.chattriggers.ctjs.api.entity.Particle
import com.chattriggers.ctjs.api.entity.PlayerMP
import com.chattriggers.ctjs.api.inventory.Inventory
import com.chattriggers.ctjs.api.inventory.Item
import com.chattriggers.ctjs.api.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.api.inventory.nbt.NBTTagList
import com.chattriggers.ctjs.api.message.ChatLib
import com.chattriggers.ctjs.api.message.TextComponent
import com.chattriggers.ctjs.api.render.Gui
import com.chattriggers.ctjs.api.render.Renderer
import com.chattriggers.ctjs.api.render.Tessellator
import com.chattriggers.ctjs.api.world.Chunk
import com.chattriggers.ctjs.api.world.Scoreboard
import com.chattriggers.ctjs.api.world.PotionEffect
import com.chattriggers.ctjs.api.world.TabList
import com.chattriggers.ctjs.api.world.World
import com.chattriggers.ctjs.api.world.block.BlockFace
import com.chattriggers.ctjs.api.world.block.Block
import com.chattriggers.ctjs.api.world.block.BlockType
import com.chattriggers.ctjs.engine.Register
import kotlin.test.Test
import kotlin.test.assertTrue
import java.lang.reflect.Modifier

class LegacyApiAliasesTest {
    @Test
    fun `common 1_8_9 method names remain exported`() {
        assertStaticMethod(Client::class.java, "getChatGUI")
        assertStaticMethod(ChatLib::class.java, "isPlayer")
        assertStaticMethod(ChatLib::class.java, "getChatMessage")
        assertStaticMethod(ChatLib::class.java, "test")
        assertTrue(
            ChatLib::class.java.methods.any {
                it.name == "clearChat" && it.parameterTypes.contentEquals(arrayOf(IntArray::class.java))
            },
            "Expected ChatLib.clearChat(IntArray) to be exported",
        )
        assertStaticMethod(KeyBind::class.java, "removeKeyBind")
        assertStaticMethod(KeyBind::class.java, "clearKeyBinds")
        for (name in listOf(
            "close", "isControlDown", "isShiftDown", "isAltDown", "getButton",
            "drawString", "drawCreativeTabHoveringString", "drawHoveringString",
        )) {
            assertMethod(Gui::class.java, name)
        }
        for (name in listOf("drawShape", "finishDraw", "getDrawMode", "setDrawMode", "retainTransforms")) {
            assertStaticMethod(Renderer::class.java, name)
        }
        assertStaticMethod(Renderer::class.java, "getColor")
        for (name in listOf(
            "disableAlpha", "enableAlpha", "alphaFunc", "enableLighting", "disableLighting", "disableDepth", "enableDepth",
            "depthFunc", "depthMask", "disableBlend", "enableBlend", "blendFunc", "tryBlendFuncSeparate",
            "enableTexture2D", "disableTexture2D", "bindTexture", "deleteTexture", "pushMatrix", "popMatrix",
            "begin", "colorize", "rotate", "translate", "scale", "pos", "tex", "draw", "getRenderPos", "drawString",
        )) {
            assertStaticMethod(Tessellator::class.java, name)
        }
        assertTrue(
            Renderer::class.java.methods.any {
                it.name == "drawString" && Modifier.isStatic(it.modifiers) &&
                    it.parameterTypes.contentEquals(arrayOf(String::class.java, Float::class.javaPrimitiveType!!, Float::class.javaPrimitiveType!!))
            },
            "Expected legacy Renderer.drawString(String, Float, Float) to be exported",
        )
        assertTrue(
            Renderer::class.java.methods.any {
                it.name == "drawStringWithShadow" && Modifier.isStatic(it.modifiers) &&
                    it.parameterTypes.contentEquals(arrayOf(String::class.java, Float::class.javaPrimitiveType!!, Float::class.javaPrimitiveType!!))
            },
            "Expected legacy Renderer.drawStringWithShadow(String, Float, Float) to be exported",
        )
        assertTrue(
            Renderer::class.java.methods.any {
                it.name == "drawPlayer" && Modifier.isStatic(it.modifiers) &&
                    it.parameterTypes.contentEquals(arrayOf(Any::class.java, Int::class.javaPrimitiveType!!, Int::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!))
            },
            "Expected Renderer.drawPlayer(Object, Int, Int, Boolean) to be exported",
        )
        assertTrue(
            Sound::class.java.methods.any {
                it.name == "setCategory" && it.parameterTypes.contentEquals(arrayOf(String::class.java))
            },
            "Expected Sound.setCategory(String) to be exported",
        )
        for (name in listOf(
            "getText", "setText", "isFormatted", "setFormatted",
            "setClick", "getClickAction", "setClickAction", "getClickValue", "setClickValue",
            "setHover", "getHoverAction", "setHoverAction", "getHoverValue", "setHoverValue",
        )) {
            assertMethod(TextComponent::class.java, name)
        }
        for (part in listOf("Cape", "Jacket", "LeftSleeve", "RightSleeve", "LeftPantsLeg", "RightPantsLeg", "Hat")) {
            assertMethod(Settings.SkinWrapper::class.java, "get$part")
            assertMethod(Settings.SkinWrapper::class.java, "set$part")
        }
        assertMethod(Settings.VideoWrapper::class.java, "getGraphics")
        assertMethod(Settings.VideoWrapper::class.java, "setGraphics")
        assertMethodWithParameter(Settings.VideoWrapper::class.java, "setClouds", Int::class.javaPrimitiveType!!)
        assertMethodWithParameter(Settings.VideoWrapper::class.java, "setParticles", Int::class.javaPrimitiveType!!)
        assertMethodWithParameter(Settings.ChatWrapper::class.java, "setVisibility", String::class.java)
        assertStaticMethod(com.chattriggers.ctjs.api.client.Player::class.java, "getRawYaw")
        assertStaticMethod(com.chattriggers.ctjs.api.client.Player::class.java, "getUUIDObj")
        assertStaticMethod(com.chattriggers.ctjs.api.client.Player::class.java, "getOpenedInventory")
        assertStaticMethod(com.chattriggers.ctjs.api.client.Player::class.java, "asEntity")
        assertTrue(
            com.chattriggers.ctjs.api.client.Player::class.java.methods.any {
                it.name == "draw" && Modifier.isStatic(it.modifiers) &&
                    it.parameterTypes.contentEquals(arrayOf(Int::class.javaPrimitiveType!!, Int::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!))
            },
            "Expected Player.draw(Int, Int, Boolean) to be exported",
        )
        assertTrue(
            PlayerMP::class.java.methods.any {
                it.name == "draw" &&
                    it.parameterTypes.contentEquals(arrayOf(Int::class.javaPrimitiveType!!, Int::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!))
            },
            "Expected PlayerMP.draw(Int, Int, Boolean) to be exported",
        )
        assertMethod(Entity::class.java, "getRider")
        for (name in listOf(
            "setAir", "setPosition", "setAngles", "setOnFire", "extinguish", "move", "setIsSilent",
            "addVelocity", "setIsSneaking", "setIsSprinting", "setIsInvisible", "isEating", "setIsEating",
        )) {
            assertMethod(Entity::class.java, name)
        }
        for (name in listOf("setX", "setY", "setZ", "multiplyVelocity", "setColor", "setAlpha", "getColor")) {
            assertMethod(Particle::class.java, name)
        }
        assertMethod(LivingEntity::class.java, "getItemInSlot")
        assertMethod(Inventory::class.java, "getItemInSlot")
        assertMethod(Inventory::class.java, "isContainer")
        assertMethod(Inventory::class.java, "doAction")
        assertMethod(Item::class.java, "isDamagable")
        assertTrue(
            Item::class.java.methods.any {
                it.name == "canDestroy" && it.parameterTypes.contentEquals(arrayOf(Block::class.java))
            },
            "Expected Item.canDestroy(Block) to be exported",
        )
        for (name in listOf(
            "getID", "getMetadata", "getRegistryName", "getUnlocalizedName", "getTextComponent", "setDamage",
            "getNBT", "getItemNBT", "getRawNBT", "getComponents",
            "getLore", "getLoreComponents", "setLore", "setName",
        )) {
            assertMethod(Item::class.java, name)
        }
        for (name in listOf("getTagId", "getTagList", "setLongArray")) {
            assertMethod(NBTTagCompound::class.java, name)
        }
        assertMethod(NBTTagList::class.java, "get")
        for (parameter in listOf<Class<*>>(String::class.java, Int::class.javaPrimitiveType!!, BlockType::class.java, Entity::class.java)) {
            assertTrue(
                Item::class.java.constructors.any { it.parameterTypes.contentEquals(arrayOf(parameter)) },
                "Expected Item constructor accepting ${parameter.simpleName}",
            )
        }
        assertMethod(Scoreboard.Score::class.java, "getPoints")
        assertMethod(PotionEffect::class.java, "isAmbient")
        assertMethod(PotionEffect::class.java, "isDurationMax")
        assertMethod(Scoreboard.Score::class.java, "setPoints")
        assertStaticMethod(BlockFace::class.java, "fromMCEnumFacing")
        assertMethod(BlockFace::class.java, "getName")
        for (name in listOf("getMetadata", "isPowered", "getRedstoneStrength")) {
            assertMethod(Block::class.java, name)
        }
        for (name in listOf(
            "getBlock", "getBlockPos", "setBlockPos", "setFace", "getID", "getRegistryName",
            "getUnlocalizedName", "getName", "getLightValue", "getDefaultState", "getDefaultMetadata",
            "canProvidePower", "isTranslucent",
        )) {
            assertMethod(Block::class.java, name)
        }
        for (parameter in listOf<Class<*>>(
            BlockType::class.java, Block::class.java, String::class.java,
            Int::class.javaPrimitiveType!!, Item::class.java,
        )) {
            assertTrue(
                Block::class.java.constructors.any { it.parameterTypes.contentEquals(arrayOf(parameter)) },
                "Expected Block constructor accepting ${parameter.simpleName}",
            )
        }
        assertMethod(BlockType::class.java, "getDefaultMetadata")
        assertMethod(BlockType::class.java, "getUnlocalizedName")
        assertStaticMethod(Scoreboard::class.java, "getScoreboardTitle")
        assertStaticMethod(TabList::class.java, "getFooterMessage")
        assertStaticMethod(TabList::class.java, "getHeaderMessage")
        assertStaticMethod(World::class.java, "getAllTileEntities")
        assertStaticMethod(World::class.java, "getAllTileEntitiesOfType")
        assertStaticMethod(World::class.java, "getMoonPhase")
        assertStaticMethod(World::class.java, "playSound")
        assertStaticMethod(World::class.java, "playRecord")
        assertStaticMethod(World::class.java, "stopAllSounds")
        assertMethod(Chunk::class.java, "getAllTileEntities")
        assertMethod(Chunk::class.java, "getAllTileEntitiesOfType")
        assertMethod(Chunk::class.java, "getSkyLightLevel")
        assertMethod(Chunk::class.java, "getBlockLightLevel")
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

    private fun assertMethodWithParameter(type: Class<*>, name: String, parameter: Class<*>) {
        assertTrue(
            type.methods.any { it.name == name && it.parameterTypes.contentEquals(arrayOf(parameter)) },
            "Expected ${type.simpleName}.$name(${parameter.simpleName}) to be exported",
        )
    }
}
