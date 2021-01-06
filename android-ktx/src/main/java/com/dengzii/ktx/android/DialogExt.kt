package com.dengzii.ktx.android

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import androidx.core.app.ComponentActivity
import androidx.lifecycle.LifecycleOwner

fun Dialog.showAutoHide() {
    if (context is LifecycleOwner) {
        (context as ComponentActivity).registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityPaused(activity: Activity) {
                    TODO("Not yet implemented")
                }

                override fun onActivityStarted(activity: Activity) {
                    TODO("Not yet implemented")
                }

                override fun onActivityDestroyed(activity: Activity) {
                    TODO("Not yet implemented")
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                    TODO("Not yet implemented")
                }

                override fun onActivityStopped(activity: Activity) {
                    TODO("Not yet implemented")
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    TODO("Not yet implemented")
                }

                override fun onActivityResumed(activity: Activity) {
                    TODO("Not yet implemented")
                }
            })
    }
}