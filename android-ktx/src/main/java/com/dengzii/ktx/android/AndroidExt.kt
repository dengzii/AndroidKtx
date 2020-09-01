package com.dengzii.ktx.android

import android.content.res.Resources

fun Float.px2dp(): Float {
    return (this / getDeviceDensity())
}

fun Float.dp2px(): Int {
    return (0.5f + this * getDeviceDensity()).toInt()
}

private fun getDeviceDensity(): Float {
    return Resources.getSystem().displayMetrics.density
}
