@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.content.Intent
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment


/**
 * Return a instance of the [Lazy] that find the [View] of specified [id].
 * @param id The id of the View.
 */
inline fun <T : View> Fragment.lazyFindView(view: View, @IdRes id: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById<T>(id)
    }
}

/**
 * Return a instance of the [Lazy], lazy initialization with the [Intent] extra of the specified key.
 * @param key The key of extra.
 */
inline fun <reified T> Fragment.intentExtra(key: String): Lazy<T?> {
    return lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getOrNull<T>(key)
    }
}

/**
 * Lazy initialization with the [Intent] extra of the specified key.
 *
 * @param key The key of Intent extra
 * @param default The default value when the key not found or type error
 * @see Fragment.intentExtra
 */
inline fun <reified T> Fragment.intentExtra(key: String, default: T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getOrDefault(key, default) ?: default
    }
}
