package com.example.testnfc

import android.content.Context
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.testnfc.ndef.NDEFFile
import java.io.File
import android.content.BroadcastReceiver
import android.content.Intent
import android.nfc.NdefRecord


class MainActivity : AppCompatActivity() {

    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = super.getApplicationContext()
        val file = File(context.filesDir, "storage")
        if (!file.exists()) {
            defaultWrite()
        }
        context.openFileInput("storage").use {
            val readBytes = it.readBytes()
            updateDisplay(readBytes.sliceArray(2 until readBytes.size))
        }
        val testButton = findViewById<Button>(R.id.test_write)
        val commandDecoder = CommandDecoder(NDEFFile(this))
        testButton.setOnClickListener {
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
                commandDecoder.decodeCommand(intArrayOf(0x000,0xD6,0x00,0x00,0x21,0x00,0x1F,0x91,0x01,0x0A,0x55,0x01,0x61,0x70,0x70,0x6C,0x65,0x2E,0x63,0x6F,0x6D,0x11,0x01,0x09,0x54,0x02,0x66,0x72,0x63,0x6F,0x75,0x63,0x6F,0x75,0x51,0x00,0x01,0x50))
            Log.d(javaClass.name, res.toHexString())

            broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val bytes = intent.getByteArrayExtra("ndef")
                    updateDisplay(bytes)
                }
            }
            LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver as BroadcastReceiver,
                IntentFilter("update-text")
            )
        }
    }

    fun trim(bytes: ByteArray): ByteArray {
        var i = bytes.size - 1
        while (i >= 0 && bytes[i].toInt() == 0) {
            --i
        }
        return bytes.sliceArray(0 .. i)
    }

    private fun updateDisplay(bytes: ByteArray) {
        Log.d(javaClass.name, "updating: ${bytes.toHexString()}")

        try {
            val textView = findViewById<TextView>(R.id.lol)

            val ndefMessage = NdefMessage(trim(bytes))
            val builder = StringBuilder()
            for (record in ndefMessage.records) {
                val uri = record.toUri()
                if (uri != null) {
                    builder.append("ndef record: $uri\n")
                } else {
                    builder.append("ndef record: ${String(record.payload)}\n")
                }
            }
            textView.text = builder.toString()
        } catch (e: Exception) {
            Log.e(javaClass.name, e.toString(), e)
            // defaultWrite()
        }
    }

    private fun defaultWrite() {
        applicationContext.openFileOutput("storage", Context.MODE_PRIVATE).use {
            it.write(intArrayOf(0x00, 0x03, 0xD0, 0x00, 0x00).toByteArray())
        }
    }

}
