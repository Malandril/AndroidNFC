package com.example.testnfc

import android.content.Context
import android.nfc.NdefMessage
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.testnfc.ndef.NDEFFile
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = super.getApplicationContext()
        val file = File(context.filesDir, "storage")
        val textView = findViewById<TextView>(R.id.lol)
        if (!file.exists()) {
            defaultWrite()
        }
        context.openFileInput("storage").use {
            val readBytes = it.readBytes()
            Log.d(javaClass.name, "Read file: ${readBytes.toHexString()}")
            try {
                val ndefMessage = NdefMessage(readBytes)
                val builder = StringBuilder()
                for (record in ndefMessage.records) {
                    val uri = record.toUri()
                    if (uri != null) {
                        builder.append("$uri\n")
                    } else {
                        builder.append("${String(record.payload)}\n")
                    }
                }
                Log.i("MainActivity", Arrays.toString(readBytes))
                textView.text = builder.toString()
            } catch (e: Exception) {
                Log.e(javaClass.name, e.toString(), e)
                // defaultWrite()
            }
        }
        val test_button = findViewById<Button>(R.id.test_write)
        val commandDecoder = CommandDecoder(NDEFFile(this))
        test_button.setOnClickListener {
            Log.d(javaClass.name, "AAAAAAAAAAA")
            commandDecoder.decodeCommand(
                intArrayOf(
                    0x00,
                    0xA4,
                    0x04,
                    0x00,
                    0x07,
                    0xD2,
                    0x76,
                    0x00,
                    0x00,
                    0x85,
                    0x01,
                    0x01,
                    0x00
                )
            )
            commandDecoder.decodeCommand(intArrayOf(0x00, 0xA4, 0x00, 0x0C, 0x02, 0xE1, 0x03))
            commandDecoder.decodeCommand(intArrayOf(0x00, 0xA4, 0x00, 0x0C, 0x02, 0xE1, 0x04))
            val res =
                commandDecoder.decodeCommand(intArrayOf(0x00, 0xD6, 0x00, 0x00, 0x06, 0x04, 0x62, 0x69, 0x74, 0x65))
            Log.d(javaClass.name, res.toHexString())
        }
    }

    private fun defaultWrite() {
        this.applicationContext.openFileOutput("storage", Context.MODE_PRIVATE).use {
            it.write(intArrayOf(0xD0, 0x00, 0x00).toByteArray())
        }
    }

}
