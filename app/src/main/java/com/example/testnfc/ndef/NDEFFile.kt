package com.example.testnfc.ndef

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.example.testnfc.toByteArray
import com.example.testnfc.toIntArray


class NDEFFile(val context: Context) {


    fun convertToArray(): IntArray {
        val readBytes = context.openFileInput("storage").readBytes()
        return readBytes.toIntArray()
    }

    fun write(ndefFile: IntArray) {
        context.openFileOutput("storage", Context.MODE_PRIVATE).use {
            it.write(ndefFile.toByteArray())
            sendMessage(ndefFile.toByteArray())
        }
    }

    private fun sendMessage(bytes: ByteArray) {
        Log.d(javaClass.name, "Broadcasting message")
        val intent = Intent("update-text")
        intent.putExtra("ndef", bytes)

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

}