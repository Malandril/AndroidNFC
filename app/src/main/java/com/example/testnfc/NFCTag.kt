package com.example.testnfc

class NFCTag {
    enum class Selected {
        NONE,
        APPLICATION,
        CC,
        NDEF
    }

    private var selected = Selected.NONE

    fun selectApplication(): ByteArray {
        selected = Selected.APPLICATION
        return NFCStatus.OK.data
    }

    fun selectCCFile(): ByteArray {
        selected = Selected.CC
        return NFCStatus.OK.data
    }

    fun selectNDEFFile(): ByteArray {
        selected = Selected.NDEF
        return NFCStatus.OK.data
    }

    fun readBinary(): ByteArray {
        when (selected) {
            Selected.CC -> return readCCFile()
            Selected.NDEF -> return readNDEFFile()
            else -> return NFCStatus.INVALID_INSTRUCTION.data
        }
    }

    fun readCCFile(): ByteArray {
        return CCFile().convertToBytes() + NFCStatus.OK.data
    }

    fun readNDEFFile(): ByteArray {
        return NFCStatus.OK.data
        // return ndefFile.read() + Status.OK.data
    }
}