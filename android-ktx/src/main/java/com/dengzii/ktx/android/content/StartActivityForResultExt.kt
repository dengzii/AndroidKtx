package com.dengzii.ktx.android.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dengzii.ktx.android.content.HiddenFragment.Companion.EXTRA_START_ACTIVITY_FOR_RESULT_PARAM
import com.dengzii.ktx.android.content.HiddenFragment.Companion.sRequestCode
import com.dengzii.ktx.android.content.HiddenFragment.Companion.sStartupParams
import com.dengzii.ktx.android.getOrDefaultCompat
import com.dengzii.ktx.currentTimeMillis

/** type alias of [AppCompatActivity.onActivityResult] */
typealias ActivityResultCallback = (requestCode: Int, resultCode: Int, data: Intent?) -> Unit

data class StartupParam(
    var requestCode: Int,
    var intent: Intent,
    var callback: ActivityResultCallback
)

/**
 * Start activity for result, and get activity result in [callback] function.
 */
inline fun <reified T : AppCompatActivity> T.startActivityForResult(
    requestCode: Int,
    intent: Intent,
    crossinline callback: ActivityResultCallback
) {
    val tag = "${this::class.java.simpleName}-$requestCode-$currentTimeMillis"
    val hiddenFragment = HiddenFragment()
    hiddenFragment.arguments = Bundle().apply {
        putString(EXTRA_START_ACTIVITY_FOR_RESULT_PARAM, tag)
    }
    // remove fragment.
    val callbackWrap =
        { requestCode1: Int, resultCode: Int, resultIntent: Intent? ->
            supportFragmentManager.beginTransaction()
                .remove(hiddenFragment)
                .commitAllowingStateLoss()
            callback.invoke(requestCode1, resultCode, resultIntent)
        }
    sStartupParams[tag] = StartupParam(requestCode, intent, callbackWrap)
    supportFragmentManager.beginTransaction()
        .add(hiddenFragment, tag)
        .commitAllowingStateLoss()
}

inline fun <reified T : AppCompatActivity> T.startActivityForResult(
    clazz: Class<out Activity>,
    crossinline callback: ActivityResultCallback
) {
    startActivityForResult(Intent(this, clazz), callback)
}

inline fun <reified T : AppCompatActivity> T.startActivityForResult(
    intent: Intent,
    crossinline callback: ActivityResultCallback
) {
    startActivityForResult(sRequestCode++, intent, callback)
}

inline fun <reified T : AppCompatActivity> T.startActivityForResult(
    requestCode: Int,
    clazz: Class<out Activity>,
    crossinline callback: ActivityResultCallback
) {
    startActivityForResult(requestCode, Intent(this, clazz), callback)
}

/**
 * The Fragment use for get activity result.
 */
class HiddenFragment : Fragment() {

    private val mTag by intentExtra<String>(EXTRA_START_ACTIVITY_FOR_RESULT_PARAM)

    companion object {
        private const val TAG = "StartActivityExt"
        const val EXTRA_START_ACTIVITY_FOR_RESULT_PARAM = "EXTRA_START_ACTIVITY_FOR_RESULT_PARAM"
        val sStartupParams = linkedMapOf<String, StartupParam>()
        var sRequestCode: Int = 0
            set(value) {
                field = if (value >= (1 shl 16)) 0 else value
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (mTag == null) {
            Log.d(TAG, "start activity for result failed, activity tag is missing.")
            return
        }
        val startupParam = sStartupParams.getOrDefaultCompat(mTag!!, null)
        if (startupParam == null) {
            Log.e(TAG, "start activity for result failed, startup parameter is missing.")
            return
        }
        startActivityForResult(startupParam.intent, startupParam.requestCode)
    }

    override fun onDetach() {
        super.onDetach()
        if (sStartupParams.containsKey(mTag!!)) {
            sStartupParams.remove(mTag!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val startupParam = sStartupParams.getOrDefaultCompat(mTag!!, null)
        if (startupParam == null) {
            Log.e(TAG, "invoke callback failed, startup parameter is missing.")
            return
        }
        if (startupParam.requestCode == requestCode) {
            startupParam.callback(requestCode, resultCode, data)
            sStartupParams.remove(mTag!!)
        }
    }
}
