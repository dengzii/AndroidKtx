@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx

import java.text.SimpleDateFormat
import java.util.*


inline fun Date.toMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.timeInMillis
}

inline fun String.toDate(dateFormat: String): Date? {
    return justTry {
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
        simpleDateFormat.parse(this)
    }
}