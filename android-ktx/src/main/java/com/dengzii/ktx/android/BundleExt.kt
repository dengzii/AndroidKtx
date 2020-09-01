@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.os.Bundle

/**
 * Return value of the [key] when the key is exists and the type is [T], otherwise [default].
 *
 * @param T The type of the value.
 * @param key The key of extra.
 * @param default The default value when key does not exists or the type is not [T].
 */
inline fun <reified T> Bundle.getOrDefault(key: String, default: T): T {
    return getOrNull<T>(key) ?: default
}

/**
 * Return value of the [key] when the key is exists and the type is [T], otherwise null.
 *
 * @param key The key of extra.
 */
inline fun <reified T> Bundle.getOrNull(key: String): T? {
    if (this.containsKey(key)) {
        val value = this.get(key)
        if (value is T) {
            return value
        }
    }
    return null
}


