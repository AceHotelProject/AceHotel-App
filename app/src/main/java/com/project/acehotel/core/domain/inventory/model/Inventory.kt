package com.project.acehotel.core.domain.inventory.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Inventory(
    val id: String,
    val name: String,
    val type: String,
    val stock: Int,
    val historyList: List<InventoryHistory>
) : Parcelable