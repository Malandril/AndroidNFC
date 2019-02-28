package com.example.testnfc.ndef

import android.content.Context
import android.nfc.NdefMessage
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
        }

    }

}