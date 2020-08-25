@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T : Activity> Activity.startActivity(block: (Intent.() -> Unit)) {
    val intent = Intent(this, T::class.java)
    block.invoke(intent)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int) {
    startActivityForResult(Intent(this, T::class.java), requestCode)
}

inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    block: (Intent.() -> Unit)
) {
    val intent = Intent(this, T::class.java)
    block.invoke(intent)
    startActivityForResult(intent, requestCode)
}
