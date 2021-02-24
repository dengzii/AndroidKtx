package com.dengzii.ktx.android

import android.Manifest
import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.RequiresPermission
import com.dengzii.ktx.ContextUtils
import com.dengzii.ktx.closeSilent
import com.dengzii.ktx.createOrExistsFile
import com.dengzii.ktx.justTry
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

inline fun Bitmap.toDrawable() = BitmapDrawable(ContextUtils.getApp().resources, this)

/**
 * Convert to round bitmap.
 * @param borderSize the border size.
 * @param borderColor the border color.
 * @param recycle weather recycle bitmap after convert.
 */
fun Bitmap.toRound(
    @IntRange(from = 0) borderSize: Int = 0,
    @ColorInt borderColor: Int = 0,
    recycle: Boolean = false
): Bitmap {
    val size = width.coerceAtMost(height)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val ret = Bitmap.createBitmap(width, height, config)
    val center = size / 2f
    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
    rectF.inset((width - size) / 2f, (height - size) / 2f)
    val matrix = Matrix()
    matrix.setTranslate(rectF.left, rectF.top)
    matrix.preScale(size.toFloat() / width, size.toFloat() / height)
    val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    shader.setLocalMatrix(matrix)
    paint.shader = shader
    val canvas = Canvas(ret)
    canvas.drawRoundRect(rectF, center, center, paint)
    if (borderSize > 0) {
        paint.shader = null
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize.toFloat()
        val radius = center - borderSize / 2f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
    if (recycle && !isRecycled) recycle()
    return ret
}

/**
 * Save the bitmap to specified path.
 * @param path the save file path.
 * @param format the image format.
 */
@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
@Throws(Exception::class)
fun Bitmap.saveTo(
    path: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    @IntRange(from = 0, to = 100) quality: Int = 100
): Boolean {
    val file = File(path)
    if (!file.createOrExistsFile()) {
        throw FileSystemException(file = file, reason = "create file failed.")
    }
    return saveTo(file, format, quality)
}

/**
 * RenderScript blur.
 * @param radius the blur radius.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Bitmap.blur(
    @FloatRange(from = 0.0, to = 25.0, fromInclusive = false) radius: Float = 10f,
    recycle: Boolean = false
): Bitmap {
    var rs: RenderScript? = null
    val ret = if (recycle) this else this.copy(this.config, true)
    try {
        rs = RenderScript.create(ContextUtils.getApp())
        rs.messageHandler = RenderScript.RSMessageHandler()
        val input =
            Allocation.createFromBitmap(
                rs, ret,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT
            )
        val output =
            Allocation.createTyped(rs, input.type)
        val blurScript =
            ScriptIntrinsicBlur.create(
                rs,
                Element.U8_4(rs)
            )
        blurScript.setInput(input)
        blurScript.setRadius(radius)
        blurScript.forEach(output)
        output.copyTo(ret)
    } finally {
        rs?.destroy()
    }
    return ret
}

/**
 * Save bitmap to file.
 * @param file the file, if exist will override, otherwise create.
 * @param quality the image quality.
 */
fun Bitmap.saveTo(
    file: File,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    @IntRange(from = 0, to = 100) quality: Int = 100
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

/**
 * Save bitmap to album
 * @param format the image format
 * @param quality the image quality
 */
@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
fun Bitmap.saveToAlbum(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    @IntRange(from = 0, to = 100) quality: Int = 100
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
            os.closeSilent()
        }
    }
}