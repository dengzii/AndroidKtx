package com.dengzii.ktx

import java.io.Closeable
import java.io.IOException

/**
 * Close a [Closeable] object silent.
 */
fun Closeable?.closeSilent() {
    if (this == null) return
    try {
        close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

/**
 * Operate [Closeable] (Stream, Cursor etc.) silent, auto close the Closeable object, ignore catch exception.
 * @param onException the [action] default return value when exception occurred.
 * @param action the [Closeable] action scope block.
 */
inline fun <T> Closeable?.operate(onException: T, action: Closeable.() -> T): T {
    if (this == null) {
        return onException
    }
    try {
        return action()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeSilent()
    }
    return onException
}