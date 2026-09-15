package com.chattriggers.ctjs.internal.engine.module

import java.io.IOException
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ModulePathsTest {
    @Test
    fun `legacy separators resolve consistently`() {
        val root = Files.createTempDirectory("ctjs-module-paths").toFile()
        assertEquals(ModulePaths.resolveEntry(root, "scripts/index.js"), ModulePaths.resolveEntry(root, "scripts\\index.js"))
    }

    @Test
    fun `entry cannot escape module folder`() {
        val root = Files.createTempDirectory("ctjs-module-paths").toFile()
        listOf("../outside.js", "C:/outside.js", "/outside.js", "..").forEach { entry ->
            assertFailsWith<IOException> { ModulePaths.resolveEntry(root, entry) }
        }
    }
}
