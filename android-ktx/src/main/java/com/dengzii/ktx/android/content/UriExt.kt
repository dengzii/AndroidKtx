package com.dengzii.ktx.android.content

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContentResolverCompat
import java.util.*

inline val Uri.isMediaDoc: Boolean
    get() = "com.android.providers.media.documents" == authority

inline val Uri.isDownloadDoc: Boolean
    get() = "com.android.providers.downloads.documents" == authority

inline val Uri.isExternalStorage: Boolean
    get() = "com.android.externalstorage.documents" == authority

inline val Uri.isContent: Boolean
    get() = "content" == scheme?.toLowerCase(Locale.getDefault())

inline val Uri.isFile: Boolean
    get() = "file" == scheme

@RequiresApi(Build.VERSION_CODES.KITKAT)
inline fun Uri.isDocumentUri(context: Context): Boolean {
    return DocumentsContract.isDocumentUri(context, this)
}

/**
 * Return the absolute path of uri.
 */
fun Uri.getRealPath(context: Context): String? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        if (this.isDocumentUri(context)) {
            when {
                this.isMediaDoc -> {
                    val docId = DocumentsContract.getDocumentId(this)
                    val split = docId.split(":")
                    val uri = when (split[0]) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> return null
                    }
                    val selectionArgs = arrayOf(split[1])
                    return uri.queryContentDataColumn(context, "_id=?", selectionArgs)
                }
                this.isDownloadDoc -> {
                    val docId = DocumentsContract.getDocumentId(this)
                    if (docId.isNullOrEmpty()) {
                        return null
                    }
                    if (docId.startsWith("raw:")) {
                        return docId.substring(4)
                    }
                    val thisPrefix = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/all_downloads",
                        "content://downloads/my_downloads"
                    )
                    for (uriPrefix in thisPrefix) {
                        val uri = ContentUris.withAppendedId(Uri.parse(uriPrefix), docId.toLong())
                        return uri.queryContentDataColumn(context, null, null)
                    }
                }
            }
        } else if (this.isContent) {
            return this.queryContentDataColumn(context, null, null)
        } else if (this.isFile) {
            return this.path
        }
    } else {
        return this.queryContentDataColumn(context, null, null)
    }
    return null
}

fun Uri.queryContentDataColumn(
    context: Context,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val projection = arrayOf("_data")
    if ("com.google.android.apps.photos.content" == this.authority) {
        if (this.lastPathSegment?.isNotEmpty() == true) {
            return this.lastPathSegment
        }
    }
    try {
        cursor = ContentResolverCompat.query(
            context.contentResolver, this, projection, selection,
            selectionArgs, null, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("_data")
            if (columnIndex > -1) {
                return cursor.getString(columnIndex)
            }
        }
    } catch (e: Exception) {
        Log.e("UriExt", "getDataColumnFromContent: ", e)
    } finally {
        cursor?.close()
    }
    return null
}