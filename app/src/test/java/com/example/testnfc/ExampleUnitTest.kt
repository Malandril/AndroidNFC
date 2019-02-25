package com.example.testnfc

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val myHostApduService = MyHostApduService()
        //TODO ccfile écriture 0X6985
        var code =
            myHostApduService.processCommandApdu(
                Utils.intsToBytes(
                    0x00, 0xA4, 0x04, 0x00, 0x07, 0xD2, 0x76, 0x00, 0x00, 0x85, 0x01, 0x01
                ), null
            )
        println(code.contentToString())
        assertTrue(code.contentEquals(Utils.intsToBytes(0x90, 0x00)))
        code =
            myHostApduService.processCommandApdu(
                Utils.intsToBytes(
                    0x00, 0xA4, 0x04, 0x00, 0x07, 0xD2, 0x76, 0x00, 0x00, 0x85, 0x01, 0x01
                ), null
            )
    }
}
