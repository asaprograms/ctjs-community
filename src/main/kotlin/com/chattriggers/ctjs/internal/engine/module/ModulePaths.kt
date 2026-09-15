package com.chattriggers.ctjs.internal.engine.module

import java.io.File
import java.io.IOException

internal object ModulePaths {
    fun resolveEntry(folder: File, entry: String): File {
        val portable = entry.replace('\\', '/')
        if (portable.startsWith('/') || Regex("^[A-Za-z]:").containsMatchIn(portable)) {
            throw IOException("Module entry must be a relative path: $entry")
        }
        val root = folder.canonicalFile
        val resolved = File(root, portable).canonicalFile
        if (!resolved.toPath().startsWith(root.toPath()) || resolved == root) {
            throw IOException("Module entry escapes its folder: $entry")
        }
        return resolved
    }
}
