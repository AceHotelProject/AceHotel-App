package com.project.acehotel.core.utils.constants

enum class InventoryType(val type: String, val display: String) {
    LINEN("linen", "Linen"),
    BED("kasur", "Kasur"),
    UNDEFINED("type", "Type")
}

val inventoryTypeList = listOf(InventoryType.LINEN.display, InventoryType.BED.display)