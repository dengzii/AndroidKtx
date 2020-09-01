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
 * Check if the given [keys] in intent extras exists, if not, [action] will execute.
 *
 * @param keys The keys that need to be checked for existence.
 * @param action The block execute when keys not all exist.
 */
inline fun Intent.checkExtraExists(vararg keys: String, action: (String) -> Unit) {
    keys.forEach {
        if (!hasExtra(it)) {
            action(it)
        }
    }
}

/**
 * Return whether specified [keys] are all exist in the intent extra data.
 * @param keys The keys need to check.
 * @return True expressed that all the keys exist, otherwise not.
 */
inline fun Intent.isExtraExists(vararg keys: String): Boolean {
    keys.forEach {
        if (!hasExtra(it)) {
            return false
        }
    }
    return true
}