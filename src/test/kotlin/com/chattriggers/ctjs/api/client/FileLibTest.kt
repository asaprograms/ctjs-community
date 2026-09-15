package com.chattriggers.ctjs.api.client

import java.io.IOException
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class FileLibTest {
    @Test
    fun `unzip extracts regular files`() {
        val root = Files.createTempDirectory("ctjs-unzip-test")
        val archive = root.resolve("module.zip")
        val destination = root.resolve("module")

        ZipOutputStream(Files.newOutputStream(archive)).use { zip ->
            zip.putNextEntry(ZipEntry("nested/index.js"))
            zip.write("register(\"tick\", () => {});".toByteArray())
            zip.closeEntry()
        }

        FileLib.unzip(archive.toString(), destination.toString())

        assertEquals("register(\"tick\", () => {});", destination.resolve("nested/index.js").readText())
    }

    @Test
    fun `unzip rejects entries outside destination`() {
        val root = Files.createTempDirectory("ctjs-unzip-test")
        val archive = root.resolve("module.zip")
        val destination = root.resolve("module")
        val escaped = root.resolve("escaped.js")

        ZipOutputStream(Files.newOutputStream(archive)).use { zip ->
            zip.putNextEntry(ZipEntry("../escaped.js"))
            zip.write("malicious".toByteArray())
            zip.closeEntry()
        }

        assertFailsWith<IOException> {
            FileLib.unzip(archive.toString(), destination.toString())
        }
        assertFalse(escaped.exists())
    }
}
