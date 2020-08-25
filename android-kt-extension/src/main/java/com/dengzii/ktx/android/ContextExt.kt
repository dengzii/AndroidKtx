@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.dengzii.ktx.android

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

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

inline fun Context.getDensityDpiCompat(): Int {
    return if (VERSION.SDK_INT >= 17) {
        resources.configuration.densityDpi
    } else {
        resources.displayMetrics.densityDpi
    }
}

fun Float.px2dp(): Float {
    return (this / getDeviceDensity())
}

fun Float.dp2px(): Int {
    return (0.5f + this * getDeviceDensity()).toInt()
}

private fun getDeviceDensity(): Float {
    return Resources.getSystem().displayMetrics.density
}