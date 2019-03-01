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
import android.nfc.FormatException


class MainActivity : AppCompatActivity() {

    // broadcast receiver to know when to update text
    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(javaClass.name, "broadcast received")
            runOnUiThread {
                Log.d(javaClass.name, "broadcast received")
                val bytes = intent.getByteArrayExtra("ndef")
                parseAndUpdateText(bytes)
            }
        }
    }
    private var retried = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retried = false
        setContentView(R.layout.activity_main)
        val context = applicationContext
        val file = File(context.filesDir, "storage")
        if (!file.exists()) {
            defaultWrite()
        }
        context.openFileInput("storage").use {
            val readBytes = it.readBytes()
            parseAndUpdateText(readBytes)
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver,
            IntentFilter("update-text")
        )

    }


    fun trim(bytes: ByteArray): ByteArray {
        var i = bytes.size - 1
        while (i >= 0 && bytes[i].toInt() == 0) {
            --i
        }
        return bytes.sliceArray(0..i)
    }

    private fun parseAndUpdateText(bytes: ByteArray) {
        Log.d(javaClass.name, "updating: ${bytes.toHexString()}")
        try {

            val ndefMessage = NdefMessage(trim(bytes.sliceArray(2 until bytes.size)))
            updateText(ndefMessage)
        } catch (e: FormatException) {
            Log.e(javaClass.name, e.toString(), e)
            // try with different options, without trim, with the length bytes and with the length bytes with trim
            try {
                val bytesWithoutSize = bytes.sliceArray(2 until bytes.size)
                updateText(NdefMessage(bytesWithoutSize))
            } catch (e: FormatException) {
                Log.e(javaClass.name, e.toString(), e)
                try {
                    updateText(NdefMessage(bytes))
                } catch (e: FormatException) {
                    Log.e(javaClass.name, e.toString(), e)
                    try{
                        updateText(NdefMessage(trim(bytes)))
                    }catch (e: Exception){
                        Log.e(javaClass.name, e.toString(), e)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(javaClass.name, e.toString(), e)
            val textView = findViewById<TextView>(R.id.lol)
            textView.text = getString(R.string.error_ndef)
        }
    }

    private fun updateText(ndefMessage: NdefMessage) {
        val textView = findViewById<TextView>(R.id.lol)
        val builder = StringBuilder()
        for (record in ndefMessage.records) {
            val uri = record.toUri()
            if (uri != null) {
                builder.append("ndef record url: $uri\n")
            } else {
                builder.append("ndef record: ${String(record.payload)}\n")
            }
        }
        textView.text = builder.toString()
    }

    private fun defaultWrite() {
        applicationContext.openFileOutput("storage", Context.MODE_PRIVATE).use {
            it.write(intArrayOf(0x00, 0x03, 0xD0, 0x00, 0x00).toByteArray())
        }
    }

}
