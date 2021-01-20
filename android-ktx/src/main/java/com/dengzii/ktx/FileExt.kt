@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

inline fun File?.isFileExists() = this != null && this.exists()

inline fun File?.isDir() = this != null && exists() && isDirectory

inline fun File?.isFile() = this != null && exists() && isFile

/**
 * Rename file.
 */
fun File.rename(newName: String): Boolean {
    if (!exists() || newName.isBlank()) {
        return false
    }
    if (newName == name) {
        return true
    }
    val newFile = File("${parent.orEmpty()}${File.separator}$newName")
    return !newFile.exists() && renameTo(newFile)
}

/**
 * Whether the file is exists, if not return whether create success.
 * @return True means the file is exist or create success, otherwise not.
 */
fun File.createOrExistsFile(): Boolean {
    if (exists()) {
        return true
    }
    val parentDirExist =
        if (parentFile != null && parentFile?.exists() == false) {
            parentFile?.mkdirs() ?: false
        } else {
            true
        }
    return parentDirExist && createNewFile()
}

/**
 * Whether the directory is exists, if not return whether create success.
 * @return True means the directory is exist or create success, otherwise not.
 */
fun File.createOrExistsDir(): Boolean {
    if (exists() && isDirectory) {
        return true
    }
    return mkdirs()
}

/**
 * Return the md5 digest of the file.
 */
fun File.md5(): ByteArray? {
    var dis: DigestInputStream? = null
    try {
        val fis = FileInputStream(this)
        var md = MessageDigest.getInstance("MD5")
        dis = DigestInputStream(fis, md)
        val buffer = ByteArray(1024 * 256)
        while (true) {
            if (dis.read(buffer) <= 0) break
        }
        md = dis.messageDigest
        return md.digest()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        dis.closeSilent()
    }
    return null
}

fun File.md5String(): String? {
    return md5()?.toHexString()
}