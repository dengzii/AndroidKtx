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

inline fun <T> Closeable?.operate(onException: T, action: Closeable.() -> T): T {
    if (this == null) {
        return onException
    }
    try {
        return action()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeIo()
    }
    return onException
}