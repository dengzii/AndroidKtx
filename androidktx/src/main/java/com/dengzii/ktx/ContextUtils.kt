package com.dengzii.ktx

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log

object ContextUtils {

    private lateinit var sApp: Application

    fun init(application: Application){
        sApp = application
    }

    fun getApp(): Application {
        if (ContextUtils::sApp.isInitialized) {
            return sApp
        }
        return getAppReflect()
    }

    @SuppressLint("PrivateApi")
    private fun getAppReflect(): Application {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val thread = getActivityThread()
        val app = activityThreadClass.getMethod("getApplication").invoke(thread)
        return app as Application
    }

    @SuppressLint("PrivateApi")
    private fun getActivityThread(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val sCurrentActivityThreadField =
                activityThreadClass.getDeclaredField("sCurrentActivityThread")
            sCurrentActivityThreadField.isAccessible = true
            return sCurrentActivityThreadField.get(null)
        } catch (e: Exception) {
            Log.e(
                "ContextUtils",
                "getActivityThreadInActivityThreadStaticField: " + e.message
            )
            null
        }
    }
}