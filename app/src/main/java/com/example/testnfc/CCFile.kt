package com.example.testnfc

class CCFile : IByteConvertible {
    var ccLen: ByteArray = Utils.intsToBytes(0x00, 0x0F);
    var mapping = Utils.intsToBytes(0x20)
    var maxLe = Utils.intsToBytes(0x00, 0x3B)
    var maxLc = Utils.intsToBytes(0x00, 0x34)
    var T = Utils.intsToBytes(0x04)
    var L = Utils.intsToBytes(0x06)
    var VFileIdentifier = Utils.intsToBytes(0xE1, 0x04)
    var VMaxNdefSize = Utils.intsToBytes(0x00, 0x32)
    var VSecurity = Utils.intsToBytes(0x00, 0x00)


    override fun convertToBytes(): ByteArray {
        return ccLen + mapping + maxLe + maxLc + T + L + VFileIdentifier + VMaxNdefSize + VSecurity
    }


}