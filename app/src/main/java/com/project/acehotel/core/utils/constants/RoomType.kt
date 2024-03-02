package com.project.acehotel.core.utils.constants

enum class RoomType(val type: String, val display: String) {
    REGULAR("regular", "Reguler"),
    EXCLUSIVE("exclusive", "Exclusive"),
    UNDEFINED("type", "Type")
}

val roomTypeList = listOf(RoomType.REGULAR.display, RoomType.EXCLUSIVE.display)

fun mapToRoomType(display: String): String {
    return when (display) {
        RoomType.REGULAR.display -> {
            RoomType.REGULAR.type
        }
        RoomType.EXCLUSIVE.display -> {
            RoomType.EXCLUSIVE.type
        }
        else -> {
            RoomType.UNDEFINED.type
        }
    }
}

fun mapToRoomDisplay(type: String): String {
    return when (type) {
        RoomType.REGULAR.type -> {
            RoomType.REGULAR.display
        }
        RoomType.EXCLUSIVE.type -> {
            RoomType.EXCLUSIVE.display
        }
        else -> {
            RoomType.UNDEFINED.display
        }
    }
}