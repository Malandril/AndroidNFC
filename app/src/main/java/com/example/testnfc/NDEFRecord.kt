package com.example.testnfc

import com.example.testnfc.ndef.NDEFRecordHeader
import com.example.testnfc.ndef.NDEFType

@ExperimentalUnsignedTypes
data class NDEFRecord(
    val header: NDEFRecordHeader,
    val typeLength: UByte,
    val payloadLength: UInt,
    val idLength: UByte,
    val type: UByteArray,
    val id: UByteArray,
    val payload: UByteArray
) {
    var parsedType = NDEFType.UNKNOWN
    var content: String = ""
}
