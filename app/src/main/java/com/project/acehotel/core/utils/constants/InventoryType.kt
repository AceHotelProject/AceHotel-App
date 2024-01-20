package com.project.acehotel.core.utils.constants

enum class InventoryType(val type: String, val display: String) {
    LINEN("linen", "Linen"),
    BED("kasur", "Kasur"),
    UNDEFINED("type", "Type")
}

fun mapToInventoryDisplay(type: String): String {
    return when (type) {
        InventoryType.BED.type -> {
            InventoryType.BED.display
        }
        InventoryType.LINEN.type -> {
            InventoryType.LINEN.display
        }
        InventoryType.UNDEFINED.type -> {
            InventoryType.UNDEFINED.display
        }
        else -> {
            InventoryType.UNDEFINED.display
        }
    }
}

fun mapToInventoryType(display: String): String {
    return when (display) {
        InventoryType.BED.display -> {
            InventoryType.BED.type
        }
        InventoryType.LINEN.display -> {
            InventoryType.LINEN.type
        }
        InventoryType.UNDEFINED.display -> {
            InventoryType.UNDEFINED.type
        }
        else -> {
            InventoryType.UNDEFINED.type
        }
    }
}

val inventoryTypeList = listOf(InventoryType.LINEN.display, InventoryType.BED.display)