package com.example.testnfc

import kotlin.experimental.and
import kotlin.experimental.or

fun IntArray.convertToInt16(): Int {
    if (this.size != 2)
        throw NumberFormatException("IntArray size must be 2 to convert to Int16")
    return this[1] or (this[0] shl 8)
}

fun IntArray.convertToInt32(): Int {
    if (this.size != 4)
        throw NumberFormatException("IntArray size must be 4 to convert to Int32")
    return (this[0] shl 32) or (this[1] shl 16) or (this[2] shl 8) or this[3]
}

fun IntArray.toByteArray(): ByteArray {
    return this.foldIndexed(ByteArray(this.size)) { i, a, v -> a.apply { set(i, v.toByte()) } }
}

fun ByteArray.toIntArray(): IntArray {
    return this.foldIndexed(IntArray(this.size)) { i, a, v ->
        a.apply {
            set(i, byteToInt(v))
        }
    }
}

fun ByteArray.toHexString(): String {
    val hexBuilder = StringBuilder()
    for (byte in this) {
        hexBuilder.append(String.format("%02X", byte))
    }
    return hexBuilder.toString()
}

fun IntArray.toHexString(): String {
    val hexBuilder = StringBuilder()
    for (d in this) {
        hexBuilder.append(String.format("%02X", d))
    }
    return hexBuilder.toString()
}


fun byteToInt(b: Byte): Int {
    if (b < 0)
        return b + 256
    else
        return b.toInt()
}
