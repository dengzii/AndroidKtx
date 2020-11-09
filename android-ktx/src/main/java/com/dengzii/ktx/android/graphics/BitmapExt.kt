package com.dengzii.ktx.android.graphics

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import com.dengzii.ktx.ContextUtils
import com.dengzii.ktx.closeIo
import com.dengzii.ktx.createOrExistsFile
import com.dengzii.ktx.justTry
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

inline fun Bitmap.toDrawable() = BitmapDrawable(ContextUtils.getApp().resources, this)

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
fun Bitmap.saveToAlbum(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): Boolean {
    val fileName = StringBuilder()
        .append(System.currentTimeMillis())
        .append("_")
        .append(hashCode())
        .append(".jpg")
        .toString()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val dirDCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            ?: return false
        val img = File(dirDCIM, fileName)
        if (!img.createOrExistsFile()) {
            return false
        }
        return justTry {
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.parse("file://" + img.absolutePath)
            ContextUtils.getApp().sendBroadcast(intent)
            return saveTo(img)
        } ?: false
    } else {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_DCIM + "/" + ContextUtils.getApp().packageName
            )
        }
        val contentUri: Uri =
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            }
        val uri =
            ContextUtils.getApp().contentResolver.insert(contentUri, contentValues) ?: return false
        var os: OutputStream? = null
        return try {
            os = ContextUtils.getApp().contentResolver.openOutputStream(uri)
            compress(format, quality, os)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            os.closeIo()
        }
    }
}