package com.example.testnfc

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.testnfc.ndef.NDEFFile
import java.io.File
import java.sql.Array
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = super.getApplicationContext()
        val file = File(context.filesDir, "storage")
        val textView = findViewById<TextView>(R.id.lol)
        if (!file.exists()) {
            context.openFileOutput("storage", Context.MODE_PRIVATE).use {
                it.write(intArrayOf(0xD0, 0x00, 0x00).toByteArray())
            }
        }
        context.openFileInput("storage").use {
            val readBytes = it.readBytes()
            Log.i("MainActivity", Arrays.toString(readBytes))
            textView.text = Arrays.toString(readBytes.toIntArray())
        }
        val test_button = findViewById<Button>(R.id.test_write)
        val commandDecoder = CommandDecoder(NDEFFile(this))
    }

}
