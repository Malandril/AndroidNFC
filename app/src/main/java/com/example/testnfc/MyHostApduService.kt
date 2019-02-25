package com.example.testnfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import java.lang.Exception

class MyHostApduService : HostApduService() {
    private val nfcCommandDecoder by lazy { CommandDecoder() }


    override fun onDeactivated(reason: Int) {
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        try {
            return commandApdu?.let {
                return nfcCommandDecoder.decodeCommand(commandApdu)
            } ?: NFCStatus.INVALID_INSTRUCTION.data
        } catch (e: Exception) {
            e.printStackTrace()
            return NFCStatus.UNKNOWN_ERROR.data
        }

    }


}
