package com.example.testnfc

import android.util.Log
import com.example.testnfc.ndef.NDEFFile

class CommandDecoder(var ndefFile: NDEFFile) {

    val ccFile by lazy { CCFile() }
    private val applicationName = intArrayOf(0xD2, 0x76, 0x00, 0x00, 0x85, 0x01, 0x01)

    var selected = SelectedType.NONE


    fun decodeCommand(commandApdu: IntArray): IntArray {
        if (commandApdu.size < 4) {
            return NFCStatus.INVALID_INSTRUCTION.data
        }
        val cla = commandApdu[0]
        val ins = commandApdu[1]
        val p1 = commandApdu[2]
        val p2 = commandApdu[3]
        if (cla != 0x00) {
            return NFCStatus.CLASS_NOT_SUPPORTED.data
        }
        when (ins) {
            0xA4 -> return decodeSelect(
                p1, p2, commandApdu[4],
                commandApdu.sliceArray(5 until commandApdu.size)
            )
            0xB0 -> return decodeRead(p2 or (p1 shl 8), commandApdu[4])
            0xD6 -> return updateBinary(
                p2 or (p1 shl 8),
                commandApdu[4],
                commandApdu.sliceArray(5 until commandApdu.size)
            )
            else -> return NFCStatus.INVALID_INSTRUCTION.data
        }

    }

    fun decodeRead(offset: Int, le: Int): IntArray {
        Log.d(javaClass.name, "read binary")
        if (le > ccFile.maxLe.convertToInt16()) {
            return NFCStatus.WRONG_LE.data
        }
        if (selected == SelectedType.SELECT_NDEFFILE) {
            return readNDEFFile(offset, le)
        } else if (selected == SelectedType.SELECT_CCFILE) {
            if (offset + le > ccFile.ccLen.convertToInt16()) {
                return NFCStatus.WRONG_LENGTH.data
            }
            return ccFile.convertToArray().sliceArray(offset until offset + le) + NFCStatus.OK.data
        }
        return NFCStatus.NO_CURRENT_EF.data
    }

    fun decodeSelectApplication(lc: Int, data: IntArray): IntArray {
        Log.d(javaClass.name, "select application")
        return if (lc > 16 || lc < 5) {
            NFCStatus.WRONG_LC.data
        } else {
            if (data.sliceArray(0 until data.size - 1).contentEquals(applicationName)) {
                selected = SelectedType.SELECT_APPLICATION
                NFCStatus.OK.data
            } else {
                NFCStatus.FILE_NOT_FOUND.data
            }
        }
    }

    fun decodeSelect(p1: Int, p2: Int, lc: Int, data: IntArray): IntArray {
        if (p1 == 0x04 && p2 == 0) {
            return decodeSelectApplication(lc, data)
        } else if (p1 == 0 && p2 == 0x0C) {
            Log.d(javaClass.name, "select file")
            if (lc != 2) {
                return NFCStatus.WRONG_LC.data
            } else if (selected == SelectedType.NONE) {
                return NFCStatus.NO_CURRENT_EF.data
            } else if (data.contentEquals(intArrayOf(0xE1, 0x03))) {
                selected = SelectedType.SELECT_CCFILE
            } else if (data.contentEquals(ccFile.VFileIdentifier)) {
                selected = SelectedType.SELECT_NDEFFILE
            } else {
                return NFCStatus.FILE_NOT_FOUND.data
            }
        } else {
            return NFCStatus.INVALID_P1P2_SELECT.data
        }
        return NFCStatus.OK.data
    }

    private fun getPaddedNDEF(): IntArray {
        Log.d(javaClass.name, "getPaddedNDEF")
        val minLength = ccFile.VMaxNdefSize.convertToInt16()
        val ndefFileBytes = ndefFile.convertToArray()
        val ndefFileSize = ndefFileBytes.size
        return when {
            ndefFileSize < minLength -> ndefFileBytes + IntArray((minLength - ndefFileSize))
            else -> ndefFileBytes
        }
    }

    fun readNDEFFile(offset: Int, length: Int): IntArray {
        Log.d(javaClass.name, "readNDEFFile")
        val ndefFileBytes = getPaddedNDEF()
        Log.d(javaClass.name, (offset + length).toString())
        if (offset + length > ndefFileBytes.size) {
            return NFCStatus.END_OF_FILE.data
        }
        if (length > ccFile.maxLe.convertToInt16()) {
            return NFCStatus.WRONG_LE.data
        }
        return ndefFileBytes.sliceArray(offset until offset + length) + NFCStatus.OK.data
    }

    fun updateBinary(offset: Int, length: Int, data: IntArray): IntArray {
        Log.d(javaClass.name, "updateNDEFFile")
        if (selected != SelectedType.SELECT_NDEFFILE)
            return NFCStatus.INVALID_P1P2_READWRITE.data
        val ndefFileBytes = getPaddedNDEF()
        if (length > ccFile.maxLc.convertToInt16()) {
            return NFCStatus.WRONG_LENGTH.data
        }
        val ndefFileBeginBytes = ndefFileBytes.sliceArray(0 until offset)
        val ndefFileEndBytes = ndefFileBytes.sliceArray(offset + length until ndefFileBytes.size)

        ndefFile.write(ndefFileBeginBytes + data + ndefFileEndBytes)
        return NFCStatus.OK.data
    }


}