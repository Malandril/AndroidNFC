package com.example.testnfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.example.testnfc.ndef.NDEFFile
import java.lang.Exception
import java.lang.StringBuilder

class MyHostApduService : HostApduService() {
    private val ndefFile by lazy { NDEFFile(this) }
    private val nfcCommandDecoder by lazy { CommandDecoder(ndefFile) }


    override fun onDeactivated(reason: Int) {
    }

    @ExperimentalUnsignedTypes
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        try {
            return commandApdu?.let {
                val result = nfcCommandDecoder.decodeCommand(commandApdu.toIntArray())
                val builder = StringBuilder()
                for (v in result) {
                    builder.append(String.format("%02X", v))
                }
                Log.d(javaClass.name, "response: $builder")
                return result.toByteArray()
            } ?: NFCStatus.INVALID_INSTRUCTION.data.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            return NFCStatus.UNKNOWN_ERROR.data.toByteArray()
        }

    }


}
