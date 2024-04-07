package com.project.acehotel.core.domain.inventory.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InventoryHistory(
    val historyId: String,
    val title: String,
    val desc: String,
    val stockChange: Int,
    val personInCharge: String,
    val date: String
) : Parcelable
