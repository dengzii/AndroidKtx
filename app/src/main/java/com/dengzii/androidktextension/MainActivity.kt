package com.dengzii.androidktextension

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.format.DateUtils
import android.text.method.KeyListener
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.dengzii.ktx.android.*
import com.dengzii.ktx.android.content.getDrawableCompat
import com.dengzii.ktx.android.content.requestSelectFile
import com.dengzii.ktx.android.content.startActivityForResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_title.setTextColorStateList {
            statePressed = android.R.color.holo_red_light
            stateNormal = android.R.color.black
            stateDisabled = R.color.colorPrimaryDark
        }
        getDrawableCompat(R.drawable.ic_launcher_background)?.let {
            tv_title.setDrawableStart(it)
        }
        tv_title.setDrawableStart(R.drawable.ic_launcher_background)
        tv_title.setDrawableEnd(R.drawable.ic_launcher_foreground)
        tv_title.setCompoundDrawableTintList {
            stateNormal = R.color.colorPrimary
            statePressed = R.color.colorAccent
        }
        bt_bitmap.antiShakeClick {
            println("===")
        }

        et_input.setAcceptCharacter("abcd")


        tv_title.setOnClickListener {
            startActivityForResult<SecondActivity> { reqCode, resCode, intent: Intent? ->
                println("$reqCode, $resCode, ${intent?.extras?.keySet()?.toTypedArray()}")
            }
        }

        bt_image.setOnClickListener {
            requestSelectFile("image/*") {
                println("=> " + it?.path)
            }
        }
    }
}