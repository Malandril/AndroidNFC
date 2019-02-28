package com.example.testnfc.ndef

data class NDEFRecordHeader(
    val MB: Boolean,
    val ME: Boolean,
    val CF: Boolean,
    val SR: Boolean,
    val IL: Boolean,
    val TNF2: Boolean,
    val TNF1: Boolean,
    val TNF0: Boolean
)
