package com.dengzii.ktx

import android.app.Application
import androidx.core.content.FileProvider

class KtxInitFileProvider : FileProvider() {
    override fun onCreate(): Boolean {
        val appContext = context?.applicationContext as? Application ?: return true
        ContextUtils.init(appContext)
        return true
    }
}