package com.example.testnfc

class CCFile {
    var ccLen = intArrayOf(0x00, 0x0F)
    var mapping = intArrayOf(0x20)
    var maxLe = intArrayOf(0x00, 0x3B)
    var maxLc = intArrayOf(0x00, 0x34)
    var T = intArrayOf(0x04)
    var L = intArrayOf(0x06)
    var VFileIdentifier = intArrayOf(0xE1, 0x04)
    var VMaxNdefSize = intArrayOf(0x02, 0x32)
    var VSecurity = intArrayOf(0x00, 0x00)


    fun convertToArray(): IntArray {
        return ccLen + mapping + maxLe + maxLc + T + L + VFileIdentifier + VMaxNdefSize + VSecurity
    }


}