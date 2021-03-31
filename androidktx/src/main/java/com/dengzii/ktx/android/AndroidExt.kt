package com.dengzii.ktx.android

import android.content.res.Resources

fun Float.px2dp(): Float {
    return (this / getDeviceDensity())
}

fun Float.dp2px(): Float {
    return (0.5f + this * getDeviceDensity())
}

private fun getDeviceDensity(): Float {
    return Resources.getSystem().displayMetrics.density
}
