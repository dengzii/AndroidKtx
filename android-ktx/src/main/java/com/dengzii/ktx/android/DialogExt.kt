package com.dengzii.ktx.android

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Auto call [Dialog.cancel] when state changed to [Lifecycle.Event.ON_DESTROY].
 * @param lifecycleOwner The lifecycle owner.
 */
@SuppressLint("RestrictedApi")
fun Dialog.showWithLifecycle(lifecycleOwner: LifecycleOwner) {

    lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                source.lifecycle.removeObserver(this)
                if (isShowing) {
                    cancel()
                }
            }
        }
    })
    show()
}