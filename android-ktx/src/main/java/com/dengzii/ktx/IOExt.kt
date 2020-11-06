package com.dengzii.ktx

import java.io.Closeable
import java.io.IOException

fun Closeable?.closeIo() {
    if (this == null) return
    try {
        close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}