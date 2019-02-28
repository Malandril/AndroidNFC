package com.example.testnfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.example.testnfc.ndef.NDEFFile
import java.lang.Exception

class MyHostApduService : HostApduService() {
    private val ndefFile by lazy { NDEFFile(this) }
    private val nfcCommandDecoder by lazy { CommandDecoder(ndefFile) }


    override fun onDeactivated(reason: Int) {
    }

    @ExperimentalUnsignedTypes
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {

        try {
            return commandApdu?.let {
                Log.d(javaClass.name, "received: ${commandApdu.toHexString()}")
                val result = nfcCommandDecoder.decodeCommand(commandApdu.toIntArray())
                Log.d(javaClass.name, "response: ${result.toHexString()}")
                return result.toByteArray()
            } ?: NFCStatus.INVALID_INSTRUCTION.data.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            return NFCStatus.UNKNOWN_ERROR.data.toByteArray()
        }

    }


}
