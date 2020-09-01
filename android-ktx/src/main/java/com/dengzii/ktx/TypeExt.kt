@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx

infix fun Boolean?.not(other: Boolean): Boolean {
    return this != other
}

inline fun String.toIntOr(default: Int): Int {
    return try {
        this.toInt()
    } catch (e: Throwable) {
        default
    }
}