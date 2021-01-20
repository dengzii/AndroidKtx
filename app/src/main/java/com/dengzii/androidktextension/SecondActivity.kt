package com.dengzii.androidktextension

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dengzii.ktx.android.showWithLifecycle


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sec)

        setResult(2, Intent().apply {
            putExtra("1", "1")
            putExtra("2", "1")
            putExtra("3", "1")
        })


        AlertDialog.Builder(this)
                .setTitle("MyDialog")
                .setMessage("message")
                .setNegativeButton("Close") { d: DialogInterface, _: Int ->
                    d.dismiss()
                }
                .setPositiveButton("Exit") { d, i ->
                    finish()
                }
                .create()
                .showWithLifecycle(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("SecondActivity.onDestroy")
    }
}