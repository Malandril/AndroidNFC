package com.example.testnfc

import com.example.testnfc.ndef.NDEFFile
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Array
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class NFCUnitTest {
    lateinit var myHostApduService: CommandDecoder
    lateinit var ndefFileData: IntArray
    @Before
    fun setup() {

        val ndefFile = mock<NDEFFile>()
        ndefFileData = intArrayOf(0xD0, 0x00, 0x00)
        whenever(ndefFile.write(any())).then {
            ndefFileData = it.getArgument(0)
            return@then null
        }
        whenever(ndefFile.convertToArray()).then {
            return@then ndefFileData
        }

        myHostApduService = CommandDecoder(ndefFile)
    }

    private fun select_application_code(): IntArray {
        return intArrayOf(0x00, 0xA4, 0x04, 0x00, 0x07, 0xD2, 0x76, 0x00, 0x00, 0x85, 0x01, 0x01, 0x00)
    }

    private fun select_ccfile_code() = intArrayOf(0x00, 0xA4, 0x00, 0x0C, 0x02, 0xE1, 0x03)

    private fun select_ndef_code() = intArrayOf(0x00, 0xA4, 0x00, 0x0C, 0x02, 0xE1, 0x04)

    @Test
    fun select_application() {
        var code =
            myHostApduService.decodeCommand(select_application_code())
        assertArrayEquals(intArrayOf(0x90, 0x00), code)
        assertEquals(SelectedType.SELECT_APPLICATION, myHostApduService.selected)
        // wrong lc
        code =
            myHostApduService.decodeCommand(
                intArrayOf(0x00, 0xA4, 0x04, 0x00, 0x03, 0xD2, 0x76, 0x00, 0x00, 0x85, 0x01, 0x01)
            )
        assertArrayEquals(NFCStatus.WRONG_LC.data, code)

        //Wrong data
        code =
            myHostApduService.decodeCommand(
                intArrayOf(
                    0x00, 0xA4, 0x04, 0x00, 0x07, 0xD2, 0x76, 0x00, 0x00, 0x85
                )
            )
        assertArrayEquals(NFCStatus.FILE_NOT_FOUND.data, code)
    }

    @Test
    fun test_select_file() {

        var res = myHostApduService.decodeCommand(select_ccfile_code())
        assertArrayEquals(NFCStatus.NO_CURRENT_EF.data, res)


        // selected application first
        myHostApduService.decodeCommand(select_application_code())

        res = myHostApduService.decodeCommand(select_ccfile_code())
        assertEquals(SelectedType.SELECT_CCFILE, myHostApduService.selected)
        assertArrayEquals(NFCStatus.OK.data, res)

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xA4, 0x00, 0x0C, 0x05, 0xE1, 0x03))
        assertArrayEquals(NFCStatus.WRONG_LC.data, res)

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xA4, 0x00, 0x0C, 0x02, 0xEA, 0x03))
        assertArrayEquals(NFCStatus.FILE_NOT_FOUND.data, res)

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xA4, 0x00, 0x0C, 0x02, 0xE1, 0x04))
        assertArrayEquals(NFCStatus.OK.data, res)
        assertEquals(SelectedType.SELECT_NDEFFILE, myHostApduService.selected)

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xA4, 0x01, 0x0C, 0x02, 0xE1, 0x04))
        assertArrayEquals(NFCStatus.INVALID_P1P2_SELECT.data, res)

    }


    @Test
    fun read_ccfile() {
        //TODO ccfile écriture 0X6985

        // Read without select
        var res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xB0, 0x00, 0x00, 0x02))
        assertArrayEquals(NFCStatus.NO_CURRENT_EF.data, res)

        myHostApduService.decodeCommand(select_application_code())

        myHostApduService.decodeCommand(select_ccfile_code())

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xB0, 0x00, 0x00, 0x0F))
        val ccfile_array = myHostApduService.ccFile.convertToArray()
        assertArrayEquals(ccfile_array + NFCStatus.OK.data, res)

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xB0, 0x00, 0x00, 0x03))
        assertArrayEquals(ccfile_array.sliceArray(0..2) + NFCStatus.OK.data, res)

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xB0, 0x00, 0x03, 0x0C))
        assertArrayEquals(ccfile_array.sliceArray(3 until ccfile_array.size) + NFCStatus.OK.data, res)

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xB0, 0x00, 0x00, 0xFF))
        assertArrayEquals(NFCStatus.WRONG_LE.data, res)

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xB0, 0x01, 0x00, 0x0F))
        assertArrayEquals(NFCStatus.WRONG_LENGTH.data, res)

    }

    @Test
    fun read_ndef() {
        //TODO ccfile écriture 0X6985

        // Read without select
        var res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xB0, 0x00, 0x00, 0x02))
        assertArrayEquals(NFCStatus.NO_CURRENT_EF.data, res)

        myHostApduService.decodeCommand(select_application_code())

        myHostApduService.decodeCommand(select_ccfile_code())

        res = myHostApduService.decodeCommand(select_ndef_code())
        print(Arrays.toString(res))

        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xB0, 0x00, 0x00, 0x03))
        print(Arrays.toString(res))
        assertArrayEquals(ndefFileData + NFCStatus.OK.data, res)
        res = myHostApduService.decodeCommand(intArrayOf(0x00, 0xD6, 0x00, 0x00, 0x06, 0x04, 0x62, 0x69, 0x74, 0x65))

        print(Arrays.toString(res))
    }


    @Test
    fun util_toint() {
        assertEquals(intArrayOf(0x20, 0x14).convertToInt16(), 8212)
        assertEquals(intArrayOf(0x02, 0x05).convertToInt16(), 517)
        assertEquals(0xFF, byteToInt(0xFF.toByte()))
        assertEquals(0xAA, byteToInt(0xAA.toByte()))
        assertEquals(0x04, byteToInt(0x04.toByte()))
    }
}
