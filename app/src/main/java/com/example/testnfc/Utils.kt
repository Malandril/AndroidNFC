package com.example.testnfc

class Utils{
    companion object {

        fun intsToBytes(vararg ints: Int): ByteArray {
            return ints.foldIndexed(ByteArray(ints.size)) { i, a, v -> a.apply { set(i, v.toByte()) } }
        }
    }
}