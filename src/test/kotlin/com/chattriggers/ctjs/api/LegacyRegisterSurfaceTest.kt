package com.chattriggers.ctjs.api

import com.chattriggers.ctjs.engine.Register
import java.lang.reflect.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals

class LegacyRegisterSurfaceTest {
    @Test
    fun `every 2_2 registration entry point remains exported`() {
        val expected = setOf(
            "registerActionBar", "registerAttackEntity", "registerBlockBreak", "registerChat",
            "registerChatComponentClicked", "registerChatComponentHovered", "registerClicked",
            "registerCommand", "registerDragged", "registerDrawBlockHighlight", "registerDropItem",
            "registerEntityDamage", "registerEntityDeath", "registerGameLoad", "registerGameUnload",
            "registerGuiClosed", "registerGuiDrawBackground", "registerGuiKey", "registerGuiMouseClick",
            "registerGuiMouseDrag", "registerGuiMouseRelease", "registerGuiOpened", "registerGuiRender",
            "registerHitBlock", "registerItemTooltip", "registerMessageSent", "registerNoteBlockChange",
            "registerNoteBlockPlay", "registerPacketReceived", "registerPacketSent", "registerPickupItem",
            "registerPlayerInteract", "registerPlayerJoined", "registerPlayerLeft", "registerPostGuiRender",
            "registerPostRenderEntity", "registerPostRenderTileEntity", "registerPreItemRender",
            "registerRenderAir", "registerRenderArmor", "registerRenderBossHealth", "registerRenderChat",
            "registerRenderCrosshair", "registerRenderDebug", "registerRenderEntity",
            "registerRenderExperience", "registerRenderFood", "registerRenderHand", "registerRenderHealth",
            "registerRenderHelmet", "registerRenderHotbar", "registerRenderItemIntoGui",
            "registerRenderItemOverlayIntoGui", "registerRenderJumpBar", "registerRenderMountHealth",
            "registerRenderOverlay", "registerRenderPlayerList", "registerRenderPortal",
            "registerRenderScoreboard", "registerRenderSlot", "registerRenderSlotHighlight",
            "registerRenderTileEntity", "registerRenderTitle", "registerRenderWorld",
            "registerScreenshotTaken", "registerScrolled", "registerServerConnect",
            "registerServerDisconnect", "registerSoundPlay", "registerSpawnParticle", "registerStep",
            "registerTick", "registerWorldLoad", "registerWorldUnload",
        )

        val exported = Register::class.java.methods
            .filter { Modifier.isStatic(it.modifiers) }
            .mapTo(mutableSetOf()) { it.name }

        assertEquals(emptySet(), expected - exported, "Missing legacy registration methods")
    }
}
