@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.os.Build

/**
 * The compat version of Map#getOrDefault for AndroidSdk version less then 24(Nougat)
 *  @see Map.getOrDefault
 */
inline fun <K, V> Map<K, V>.getOrDefaultCompat(key: K, default: V): V {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        getOrDefault(key, default)
    } else {
        if (containsKey(key)) {
            get(key) ?: default
        } else {
            default
        }
    }
}

inline fun <K, V> MutableMap<K, V>.put(pair: Pair<K, V>) {
    put(pair.first, pair.second)
}

inline fun <K, V> MutableMap<K, V>.put(entry: Map.Entry<K, V>) {
    put(entry.key, entry.value)
}

inline fun <K, V> MutableMap<K, V>.put(vararg pairs: Pair<K, V>) {
    putAll(pairs)
}

inline fun <K, V> Map<K, V?>.filterNullValue(): Map<K, V> {
    val result = mutableMapOf<K, V>()
    forEach { entry ->
        entry.value?.let {
            result[entry.key] = it
        }
    }
    return result
}