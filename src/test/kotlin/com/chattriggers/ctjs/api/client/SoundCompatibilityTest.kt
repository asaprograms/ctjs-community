package com.chattriggers.ctjs.api.client

import com.chattriggers.ctjs.CTJS
import net.minecraft.server.packs.PackType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SoundCompatibilityTest {
    @Test
    fun `legacy singular lowercase sound categories resolve`() {
        val expected = mapOf(
            "master" to Sound.Category.MASTER,
            "music" to Sound.Category.MUSIC,
            "record" to Sound.Category.RECORDS,
            "weather" to Sound.Category.WEATHER,
            "block" to Sound.Category.BLOCKS,
            "hostile" to Sound.Category.HOSTILE,
            "neutral" to Sound.Category.NEUTRAL,
            "player" to Sound.Category.PLAYERS,
            "ambient" to Sound.Category.AMBIENT,
        )

        for ((name, category) in expected) {
            assertEquals(category, Sound.Category.from(name))
            assertEquals(category, Sound.Category.from(name.uppercase()))
        }
        assertFailsWith<IllegalArgumentException> { Sound.Category.from("invalid") }
    }

    @Test
    fun `generated sound resource pack supports lifecycle calls`() {
        val pack = Sound.CTResourcePack

        assertEquals(CTJS.MOD_ID, pack.packId())
        assertEquals(setOf(CTJS.MOD_ID), pack.getNamespaces(PackType.CLIENT_RESOURCES))
        assertEquals(null, pack.getRootResource("pack.mcmeta"))
        assertEquals(
            null,
            pack.getMetadataSection(net.minecraft.server.packs.metadata.MetadataSectionType("test", com.mojang.serialization.Codec.STRING)),
        )
        pack.close()
    }
}
