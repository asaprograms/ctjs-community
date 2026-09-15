package com.chattriggers.ctjs.api.client

import com.chattriggers.ctjs.CTJS
import net.minecraft.util.Util
import java.io.*
import java.net.UnknownHostException
import java.nio.charset.Charset
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object FileLib {
    /**
     * Writes a file to folder in modules.
     *
     * @param importName name of the import
     * @param fileName name of the file
     * @param toWrite string to write in file
     * @param recursive whether to create folders to the file location if they don't exist
     */
    @JvmStatic
    @JvmOverloads
    fun write(importName: String, fileName: String, toWrite: String, recursive: Boolean = false) {
        write(absoluteLocation(importName, fileName), toWrite, recursive)
    }

    /**
     * Writes a file to anywhere on the system.
     * Use "./" for the ".minecraft" folder.
     *
     * @param fileLocation the location and file name
     * @param toWrite string to write in file
     * @param recursive whether to create folders to the file location if they don't exist
     */
    @JvmStatic
    @JvmOverloads
    fun write(fileLocation: String, toWrite: String, recursive: Boolean = false) {
        File(fileLocation).apply {
            if (recursive && !exists()) {
                parentFile.mkdirs()
            }
        }.writeText(toWrite)
    }

    /**
     * Writes a file to folder in modules.
     *
     * @param importName name of the import
     * @param fileName name of the file
     * @param toAppend string to append in file
     */
    @JvmStatic
    fun append(importName: String, fileName: String, toAppend: String) {
        append(absoluteLocation(importName, fileName), toAppend)
    }

    /**
     * Writes a file to anywhere on the system.
     * Use "./" for the ".minecraft" folder.
     *
     * @param fileLocation the location and file name
     * @param toAppend string to append in file
     */
    @JvmStatic
    fun append(fileLocation: String, toAppend: String) {
        File(fileLocation).appendText(toAppend)
    }

    /**
     * Reads a file from folder in modules.
     * Returns null if file is not found.
     *
     * @param importName name of the import
     * @param fileName name of the file
     * @return the string in the file, or null if not found
     */
    @JvmStatic
    fun read(importName: String, fileName: String): String? {
        return read(File(absoluteLocation(importName, fileName)))
    }

    /**
     * Reads a file from anywhere on the system.
     * Use "./" for the ".minecraft" folder.
     * Returns null if file is not found.
     *
     * @param fileLocation the location and file name
     * @return the string in the file, or null if not found
     */
    @JvmStatic
    fun read(fileLocation: String): String? {
        return read(File(fileLocation))
    }

    /**
     * Reads a file from anywhere on the system using java.io.File.
     *
     * @param file the java.io.File to read
     * @return the string in the file, or null if not found
     */
    @JvmStatic
    fun read(file: File): String? {
        return try {
            file.readText()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Determines if a file or directory exists at the specified location
     *
     * @param importName name of the import
     * @param fileName name of the file
     * @return if the file exists
     */
    @JvmStatic
    fun exists(importName: String, fileName: String): Boolean {
        return exists(absoluteLocation(importName, fileName))
    }

    /**
     * Determines if a file or directory exists at the specified location
     *
     * @param fileLocation the path of the file
     * @return if the file exists
     */
    @JvmStatic
    fun exists(fileLocation: String): Boolean {
        return File(fileLocation).exists()
    }

    /**
     * Determines if a file or directory exists at the specified location
     *
     * @param importName name of the import
     * @param fileName name of the file
     * @return if the location is a directory
     */
    @JvmStatic
    fun isDirectory(importName: String, fileName: String): Boolean {
        return isDirectory(absoluteLocation(importName, fileName))
    }

    /**
     * Determines if a file or directory exists at the specified location
     *
     * @param fileLocation the path of the file
     * @return if the location is a directory
     */
    @JvmStatic
    fun isDirectory(fileLocation: String): Boolean {
        return File(fileLocation).isDirectory
    }

    /**
     * Gets the contents of a url as a string.
     *
     * @param theUrl the url to get the data from
     * @param userAgent the user agent to use in the connection
     * @return the string stored in the url content
     */
    @Throws(UnknownHostException::class)
    @JvmStatic
    @JvmOverloads
    fun getUrlContent(theUrl: String, userAgent: String? = "Mozilla/5.0"): String {
        val conn = CTJS.makeWebRequest(theUrl, userAgent)

        return conn.getInputStream().use {
            it.readBytes()
        }.toString(Charset.forName("UTF-8"))
    }

    /**
     * Deletes a file at the specified location
     *
     * @param importName name of the import
     * @param fileName name of the file
     * @return if the file was deleted
     */
    @JvmStatic
    fun delete(importName: String, fileName: String): Boolean {
        return delete(absoluteLocation(importName, fileName))
    }

    /**
     * Deletes a file at the specified location
     *
     * @param fileLocation the path of the file
     * @return if the file was deleted
     */
    @JvmStatic
    fun delete(fileLocation: String): Boolean {
        return File(fileLocation).delete()
    }

    /**
     * Deletes a directory at the specified location
     *
     * @param dir the directory to delete
     * @return if the directory was deleted
     */
    @JvmStatic
    fun deleteDirectory(dir: String): Boolean {
        return deleteDirectory(File(dir))
    }

    /**
     * Deletes a directory at the specified location
     *
     * @param dir the directory to delete
     * @return if the directory was deleted
     */
    @JvmStatic
    fun deleteDirectory(dir: File): Boolean {
        return dir.deleteRecursively()
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exist).
     * @param zipFilePath the zip file path
     * @param destDirectory the destination directory
     * @throws IOException IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun unzip(zipFilePath: String, destDirectory: String) {
        val destination = File(destDirectory).canonicalFile
        destination.mkdirs()
        val destinationPrefix = destination.path + File.separator

        ZipInputStream(BufferedInputStream(FileInputStream(zipFilePath))).use { zipIn ->
            var entry: ZipEntry? = zipIn.nextEntry
            while (entry != null) {
                val output = File(destination, entry.name).canonicalFile
                if (output != destination && !output.path.startsWith(destinationPrefix)) {
                    throw IOException("Zip entry escapes destination: ${entry.name}")
                }

                if (entry.isDirectory) {
                    output.mkdirs()
                } else {
                    extractFile(zipIn, output)
                }

                zipIn.closeEntry()
                entry = zipIn.nextEntry
            }
        }
    }

    // helper method for unzipping
    @Throws(IOException::class)
    @JvmStatic
    private fun extractFile(zipIn: ZipInputStream, output: File) {
        output.parentFile?.mkdirs()
        BufferedOutputStream(FileOutputStream(output)).use { stream ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var read = zipIn.read(buffer)
            while (read != -1) {
                stream.write(buffer, 0, read)
                read = zipIn.read(buffer)
            }
        }
    }

    private fun absoluteLocation(importName: String, fileLocation: String): String {
        return CTJS.MODULES_FOLDER + File.separator + importName + File.separator + fileLocation
    }

    /**
     * Encodes a string to a base64 string
     *
     * @param toEncode string to encode
     * @return base64 encoded string
     */
    @JvmStatic
    fun encodeBase64(toEncode: String): String {
        return Base64.getEncoder().encodeToString(toEncode.toByteArray())
    }

    /**
     * Decodes a base64 string to a string
     *
     * @param toDecode base64 encoded string to decode
     * @return decoded string
     */
    @JvmStatic
    fun decodeBase64(toDecode: String): String {
        return String(Base64.getDecoder().decode(toDecode))
    }

    /**
     * Opens a url in the default browser
     *
     * @param url the url to open
     */
    @JvmStatic
    fun open(url: String) {
        Util.getPlatform().openUri(url)
    }

    /**
     * Opens a path in the file explorer
     *
     * @param path the path to open
     */
    @JvmStatic
    fun open(path: File) {
        Util.getPlatform().openFile(path)
    }
}
