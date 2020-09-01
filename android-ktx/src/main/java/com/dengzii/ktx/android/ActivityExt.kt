@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

/**
 * Hide Activity's toolbar
 */
inline fun AppCompatActivity.hideToolbar() {
    this.supportActionBar?.hide()
}

/**
 * Show Activity's toolbar
 */
inline fun AppCompatActivity.showToolbar() {
    this.supportActionBar?.show()
}

/**
 * Return a instance of the [Lazy] that find the [View] of specified [id].
 * @param id The of the View.
 */
inline fun <T : View> Activity.lazyFindView(@IdRes id: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        findViewById<T>(id)
    }
}

/**
 * Return a instance of the [Lazy], lazy initialization with the [Intent] extra of the specified key.
 * @param key The key of extra.
 */
inline fun <reified T> Activity.intentExtra(key: String): Lazy<T?> {
    return lazy(LazyThreadSafetyMode.NONE) {
        intent.extras?.getOrNull<T>(key)
    }
}

/**
 * Lazy initialization with the [Intent] extra of the specified key.
 * @see intentExtra
 * @param key The key of Intent extra
 * @param default The default value when the key not found or type error
 */
inline fun <reified T> Activity.intentExtra(key: String, default: T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        intent.extras?.getOrDefault(key, default) ?: default
    }
}

inline fun Activity.getDisplayDensityDpi(): Int {
    val metrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.densityDpi
}

/**
 * Set the status bar's light mode.
 * @param isLightMode True to set status bar light mode, false otherwise.
 */
inline fun Activity.setStatusBarLightMode(isLightMode: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = window.decorView
        var vis = decorView.systemUiVisibility
        vis = if (isLightMode) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = vis
    }
}

/**
 * Start a fragment transaction, execute [action] and commit.
 * @param action The [FragmentTransaction] action scope block.
 */
inline fun AppCompatActivity.beginFragmentTransaction(action: FragmentTransaction.() -> Unit) {
    val transaction = supportFragmentManager.beginTransaction()
    action(transaction)
    transaction.commit()
}

/**
 * Simplest start an Activity without intent extra.
 * @param T The type of activity
 */
inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

/**
 * Start Activity with intent block.
 *
 * @param block The intent action scope block.
 */
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
