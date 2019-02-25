package com.example.testnfc

import android.util.Log

class CommandDecoder {
    enum class SELECT_TYPE {
        SELECT_APPLICATION,
        SELECT_CCFILE,
        SELECT_NDEFFILE,
        NONE
    }

    var selected = SELECT_TYPE.NONE


    fun decodeCommand(commandApdu: ByteArray): ByteArray {
        // SELECT APP = { 0x00, 0xA4, 0x04, 0x00, 0x07, /*DATA*/0xD2, 0x76, 0x00, 0x00, 0x85, 0x01, 0x01, /*Le*/0x00 }
        val SELECT_APPLICATION_HEADER = Utils.intsToBytes(0x00, 0xA4, 0x04)
        if (commandApdu.size < 4) {
            return NFCStatus.INVALID_INSTRUCTION.data
        }
        val cla = commandApdu[0]
        val ins = commandApdu[1]
        val p1 = commandApdu[2]
        val p2 = commandApdu[3]
        if (cla != 0x00.toByte()) {
            return NFCStatus.CLASS_NOT_SUPPORTED.data
        }
        when (ins) {
            0xA4.toByte() -> return decodeSelect(
                p1,
                p2,
                commandApdu[4],
                commandApdu.sliceArray(5 until commandApdu.size)
            )
            else -> return NFCStatus.INVALID_INSTRUCTION.data
        }

    }

    fun decodeSelect(p1: Byte, p2: Byte, lc: Byte, data: ByteArray): ByteArray {
        Log.d(javaClass.name, "select")
        when {
            lc != 7.toByte() && lc != 2.toByte() -> return NFCStatus.WRONG_LC.data
            data.contentEquals(Utils.intsToBytes(0xD2, 0x76, 0x00, 0x00, 0x85, 0x01, 0x01)) -> selected =
                SELECT_TYPE.SELECT_APPLICATION
            else -> return NFCStatus.FILE_OR_APPLICATION_NOT_FOUND.data
        }
        return NFCStatus.OK.data

    }

}