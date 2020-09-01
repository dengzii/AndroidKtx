package com.dengzii.ktx.android

import android.content.SharedPreferences

inline fun <T : SharedPreferences> T.edit(action: T.() -> Unit) {
    val editor = edit()
    action()
    editor.apply()
}
