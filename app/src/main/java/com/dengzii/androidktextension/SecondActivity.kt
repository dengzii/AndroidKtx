package com.dengzii.androidktextension

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setResult(2, Intent().apply {
            putExtra("1","1")
            putExtra("2","1")
            putExtra("3","1")
        })
    }
}