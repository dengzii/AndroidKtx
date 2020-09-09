package com.dengzii.androidktextension

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dengzii.ktx.android.content.requestSelectFile
import com.dengzii.ktx.android.content.startActivityForResult
import com.dengzii.ktx.android.view.setCompoundDrawableTintList
import com.dengzii.ktx.android.view.setDrawableEnd
import com.dengzii.ktx.android.view.setDrawableStart
import com.dengzii.ktx.android.view.setTextColorStateList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_title.setTextColorStateList {
            statePressed = android.R.color.holo_red_light
            stateNormal = android.R.color.black
            stateDisabled = R.color.colorPrimaryDark
        }
        tv_title.setDrawableStart(R.drawable.ic_launcher_background)
        tv_title.setDrawableEnd(R.drawable.ic_launcher_foreground)
        tv_title.setCompoundDrawableTintList {
            stateNormal = R.color.colorPrimary
            statePressed = R.color.colorAccent
        }
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