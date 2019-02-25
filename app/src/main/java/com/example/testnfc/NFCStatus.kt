package com.example.testnfc

enum class NFCStatus(val data : ByteArray) {
    OK(Utils.intsToBytes(0x90, 0x00)),

    CLASS_NOT_SUPPORTED(Utils.intsToBytes(0x6E, 0x00)), //invalid CLA
    WRONG_LC(Utils.intsToBytes(0x67, 0x00)), // invalid Lc
    WRONG_LE(Utils.intsToBytes(0x6C, 0x00)), // invalid Le
    INVALID_INSTRUCTION(Utils.intsToBytes(0x6D, 0x00)), // invalid INS
    FILE_OR_APPLICATION_NOT_FOUND(Utils.intsToBytes(0x6A, 0x82)), // invalid LiD/AiD in select
    INVALID_P1P2_SELECT(Utils.intsToBytes(0x6A, 0x86)), // invalid P1P2 for select
    INVALID_P1P2_READWRITE_BINARY(Utils.intsToBytes(0x6B, 0x00)), // invalid P1P2 for read/write binary
    INCONSISTENT_NC_WITH_P1P2(Utils.intsToBytes(0x6A, 0x87)), // invalid Lc offset for read/write binary
    END_OF_FILE(Utils.intsToBytes(0x62, 0x82)), // invalid Le offset for read/write binary
    NO_CURRENT_EF(Utils.intsToBytes(0x69, 0x86)), // read/write binary with no selected file
    UNKNOWN_ERROR(Utils.intsToBytes(0x42, 0x69)), // Error thrown when something went wrong and didn't detect where
}
