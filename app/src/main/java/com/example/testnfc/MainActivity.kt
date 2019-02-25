package com.example.testnfc

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = super.getApplicationContext()
        val file = File(context.filesDir, "storage")
        val textView = findViewById<TextView>(R.id.lol)
        context.openFileOutput("storage", Context.MODE_PRIVATE).use {
            it.write("salut".toByteArray())
        }
        context.openFileInput("storage").use {
            val readBytes = it.readBytes()
            Log.i("MainActivity",String(readBytes))
            textView.text = String(readBytes)
        }

    }

}
