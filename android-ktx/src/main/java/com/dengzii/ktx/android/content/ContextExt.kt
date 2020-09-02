@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.dengzii.ktx.android.content

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.io.InputStream

inline fun Context.getColorCompat(@ColorRes resId: Int): Int {
    return ResourcesCompat.getColor(resources, resId, theme)
}

inline fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, resId, theme)
}

inline fun Context.getColorStateListCompat(@ColorRes resId: Int): ColorStateList? {
    return ResourcesCompat.getColorStateList(resources, resId, theme)
}

inline fun Context.checkSelfPermissionCompat(permission: String): Int {
    return ContextCompat.checkSelfPermission(this, permission)
}

/**
 * Return the status bar's height.
 */
inline fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = Resources.getSystem().getIdentifier(
        "status_bar_height", "dimen",
        "android"
    )
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * Return the screen's height.
 */
inline fun Context.getScreenHeight(): Int {
    var screenHeight = 0
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    wm?.let {
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        screenHeight = metrics.heightPixels
    }
    return screenHeight
}

/**
 * Return the target screen density being rendered to.
 *
 * @return The density expressed as dpi
 */
inline fun Context.getConfigDensityDpi(): Int {
    return if (VERSION.SDK_INT >= 17) {
        resources.configuration.densityDpi
    } else {
        resources.displayMetrics.densityDpi
    }
}

/**
 * Load asset by file name.
 * @param fileName The asset file name
 */
inline fun Context.loadAsset(fileName: String): InputStream {
    return this.assets.open(fileName)
}

inline fun Context.inflate(
    @LayoutRes resId: Int,
    parent: ViewGroup?,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(this).inflate(resId, parent, attachToRoot)
}

/**
 * Show short time toast.
 * @param text The toast text.
 */
inline fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}