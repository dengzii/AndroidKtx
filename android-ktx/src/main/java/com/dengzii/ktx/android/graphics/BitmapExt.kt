package com.dengzii.ktx.android.graphics

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import androidx.annotation.RequiresPermission
import com.dengzii.ktx.ContextUtils
import com.dengzii.ktx.createOrExistsFile
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

inline fun Bitmap.toDrawable() = BitmapDrawable(ContextUtils.sApp.resources, this)

@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
@Throws(Exception::class)
fun Bitmap.saveTo(
    path: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): Boolean {
    val file = File(path)
    if (!file.createOrExistsFile()) {
        throw FileSystemException(file = file, reason = "create file failed.")
    }
    return saveTo(file)
}

fun Bitmap.saveTo(
    file: File,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): Boolean {
    return try {
        val outputStream = FileOutputStream(file)
        compress(format, quality, outputStream)
        outputStream.flush()
        outputStream.close()
        true
    } catch (e: java.lang.Exception) {
        false
    }
}

@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
fun Bitmap.saveToAlbum(): Boolean {
    val dirDCIM = ContextUtils.sApp.getExternalFilesDir(Environment.DIRECTORY_DCIM)?.absoluteFile
        ?: return false
    val imgName = StringBuilder()
        .append(System.currentTimeMillis())
        .append("_")
        .append(hashCode())
        .append(".jpg")
        .toString()
    val img = File(dirDCIM, imgName)
    if (!img.createOrExistsFile()) {
        return false
    }
    println(img.absoluteFile)
    try {

    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        return false
    }
    return saveTo(img)
}