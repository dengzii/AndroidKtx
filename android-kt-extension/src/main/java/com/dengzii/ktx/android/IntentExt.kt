@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.content.Intent

inline fun Intent.getStringExtraOrDefault(key: String, default: String): String {
    return getStringExtra(key) ?: default
}

inline fun Intent.getStringExtraOrElse(key: String, action: () -> String): String {
    return getStringExtra(key) ?: action()
}

inline fun Intent.getStringArrayOrDefault(
    key: String,
    default: ArrayList<String>
): ArrayList<String> {
    return getStringArrayListExtra(key) ?: default
}

inline fun Intent.getStringArrayOrElse(
    key: String,
    action: () -> ArrayList<String>
): ArrayList<String> {
    return getStringArrayListExtra(key) ?: action()
}

/**
 * Check if the given [keys] are all exists, if not, [action] will invoke.
 */
inline fun Intent.checkExtraExists(vararg keys: String, action: () -> Unit) {
    if (!checkExtraExists(*keys)){
        action()
    }
}

inline fun Intent.checkExtraExists(vararg keys: String): Boolean {
    keys.forEach {
        if (!hasExtra(it)) {
            return false
        }
    }
    return true
}