package com.project.acehotel.core.utils.constants

enum class RoomStatus(val status: String, val display: String) {
    READY("Ready", "Siap digunakan"),
    USED("Used", "Sudah terhuni"),
    BROKEN("Broken", "Dalam perbaikan"),
    UNDEFINED("Status", "Status kamar"),
}