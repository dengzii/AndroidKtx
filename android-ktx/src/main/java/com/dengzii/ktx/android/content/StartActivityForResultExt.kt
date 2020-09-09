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
import java.io.File

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
    intent: Intent,
    requestCode: Int? = null,
    crossinline callback: ActivityResultCallback
) {
    val rCode = requestCode ?: sRequestCode
    val tag = "${this::class.java.simpleName}-$rCode-$currentTimeMillis"
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
    sStartupParams[tag] = StartupParam(rCode, intent, callbackWrap)
    supportFragmentManager.beginTransaction()
        .add(hiddenFragment, tag)
        .commitAllowingStateLoss()
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.startActivityForResult(
    crossinline callback: ActivityResultCallback
) {
    startActivityForResult(Intent(this, T::class.java), callback)
}

inline fun <reified T : AppCompatActivity> T.startActivityForResult(
    intent: Intent,
    crossinline callback: ActivityResultCallback
) {
    startActivityForResult(intent, sRequestCode, callback)
}

inline fun <reified T : AppCompatActivity> T.startActivityForResult(
    requestCode: Int,
    clazz: Class<out Activity>,
    crossinline callback: ActivityResultCallback
) {
    startActivityForResult(Intent(this, clazz), requestCode, callback)
}

/**
 * Start system file select activity, return selected file in [action]
 *
 * @param mimeType The file MIME type need to filter.
 * @param action The callback of result
 */
inline fun <reified T : AppCompatActivity> T.requestSelectFile(
    mimeType: String,
    crossinline action: (File?) -> Unit
) {

    val intent = Intent()
    intent.action = Intent.ACTION_GET_CONTENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = mimeType
    startActivityForResult(intent) { _: Int, resultCode: Int, intent1: Intent? ->
        if (resultCode == Activity.RESULT_OK && intent1 != null) {
            val uri = intent1.data
            if (uri == null) {
                action.invoke(null)
                return@startActivityForResult
            }
            val path = uri.getRealPath(this)
            if (path == null) {
                action.invoke(null)
                return@startActivityForResult
            }
            action.invoke(File(path))
        } else {
            action.invoke(null)
        }
    }
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
            get() = field++
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
