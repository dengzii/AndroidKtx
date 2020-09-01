package com.dengzii.ktx.android

import android.content.SharedPreferences
import java.lang.RuntimeException


inline fun <T : SharedPreferences> T.edit(action: T.() -> Unit) {
    val editor = edit()
    action()
    editor.apply()
}

inline fun <reified T : Any?> Preferences.preference(
    defaultValue: T,
    keyName: String? = null
): Preferences.Preference<T> {
    return when (defaultValue) {
        is Int?, is Boolean?, is Float?, is String?, is Set<*>? -> {
            Preference<T>(defaultValue, T::class, keyName)
        }
        else -> throw RuntimeException("Unsupported type ${T::class}")
    }
}