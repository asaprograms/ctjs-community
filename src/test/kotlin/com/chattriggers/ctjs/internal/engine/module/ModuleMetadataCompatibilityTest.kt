package com.chattriggers.ctjs.internal.engine.module

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ModuleMetadataCompatibilityTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `reads ChatTriggers 2 metadata`() {
        val metadata = decode("legacy-metadata.json")

        assertEquals("LegacyFixture", metadata.name)
        assertEquals("ChatTriggers contributors", metadata.creator)
        assertEquals("index.js", metadata.entry)
        assertEquals(arrayListOf("Vigilance"), metadata.requires)
        assertEquals(arrayListOf("README.md"), metadata.ignored)
    }

    @Test
    fun `accepts legacy author alias`() {
        val metadata = decode("legacy-author-metadata.json")

        assertEquals("Legacy module author", metadata.creator)
    }

    private fun decode(name: String): ModuleMetadata {
        val resource = checkNotNull(javaClass.getResource("/compatibility/$name"))
        return json.decodeFromString(resource.readText())
    }
}
