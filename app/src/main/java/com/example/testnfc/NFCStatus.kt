package com.example.testnfc

enum class NFCStatus(val data : IntArray) {
    OK(intArrayOf(0x90, 0x00)),

    CLASS_NOT_SUPPORTED(intArrayOf(0x6E, 0x00)), //invalid CLA
    WRONG_LC(intArrayOf(0x67, 0x00)), // invalid Lc
    WRONG_LENGTH(intArrayOf(0x67, 0x00)), // invalid length
    WRONG_LE(intArrayOf(0x6C, 0x00)), // invalid Le
    INVALID_INSTRUCTION(intArrayOf(0x6D, 0x00)), // invalid instruction
    FILE_NOT_FOUND(intArrayOf(0x6A, 0x82)), // invalid AiD in select
    INVALID_P1P2_SELECT(intArrayOf(0x6A, 0x86)), // invalid P1P2 for select
    INVALID_P1P2_READWRITE(intArrayOf(0x6B, 0x00)), // invalid P1P2 for read/write binary
    INCONSISTENT_LC_WITH_P1P2(intArrayOf(0x6A, 0x87)), // invalid Lc offset for read/write binary
    END_OF_FILE(intArrayOf(0x62, 0x82)), // invalid Le offset for read/write binary
    NO_CURRENT_EF(intArrayOf(0x69, 0x86)), //no selected file
    UNKNOWN_ERROR(intArrayOf(0x64, 0x00)), // Error thrown when something went wrong and don't know where
}
