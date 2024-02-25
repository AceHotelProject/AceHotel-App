package com.project.acehotel.core.utils.constants

enum class RoomType(val type: String, val display: String) {
    REGULAR("reguler", "Reguler"),
    EXCLUSIVE("exclusive", "Exclusive"),
    UNDEFINED("type", "Type")
}

val roomTypeList = listOf(RoomType.REGULAR.display, RoomType.EXCLUSIVE.display)