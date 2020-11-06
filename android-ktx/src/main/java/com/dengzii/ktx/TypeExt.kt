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


private val hexDigits =
    charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

fun ByteArray?.toHexString(): String? {
    if (this == null || size <= 0) return null
    val ret = CharArray(size shl 1)
    var j = 0
    for (i in 0 until size){
        ret[j++] = hexDigits[this[i].toInt() ushr 4 and 0x0f]
        ret[j++] = hexDigits[this[i].toInt() and 0x0f]
    }
    return String(ret)
}