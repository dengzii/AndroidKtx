package com.dengzii.ktx


inline val currentTimeMillis: Long
    get() = System.currentTimeMillis()

/**
 * Returns the return value of the function [action], if an exception occurs while executing [action],
 * null will be returned, the exception detail will be ignored.
 */
inline fun <T> justTry(action: () -> T): T? {
    return try {
        action()
    } catch (e: Throwable) {
        null
    }
}
