@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx

infix fun Boolean?.not(other: Boolean): Boolean {
    return this != other
}

inline fun String.toIntOr(default: Int) = toIntOrNull() ?: default

inline fun String.toFloatOr(default: Float) = toFloatOrNull() ?: default

inline fun String.toLongOr(default: Long) = toLongOrNull() ?: default

inline fun String.toDoubleOr(default: Double) = toDoubleOrNull() ?: default

inline fun String.toShortOr(default: Float) = toShortOrNull() ?: default


