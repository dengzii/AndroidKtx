package com.dengzii.ktx


inline val currentTimeMillis: Long
    get() = System.currentTimeMillis()

inline fun <T> justTry(action: () -> T): T? {
    return try {
        action()
    } catch (e: Throwable) {
        null
    }
}
