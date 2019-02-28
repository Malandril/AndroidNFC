package com.example.testnfc.ndef

@ExperimentalUnsignedTypes
data class NDEFRecord(
    val header: NDEFRecordHeader,
    val typeLength: Int,
    val payloadLength: Int,
    val idLength: Int,
    val type: IntArray,
    val id: IntArray,
    val payload: IntArray
) {
    var parsedType = NDEFType.UNKNOWN
    var content: String = ""

}
