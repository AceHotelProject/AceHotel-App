package com.project.acehotel.core.domain.inventory.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
    val status: String,
    val tid: String,
    val inventoryId: String,
    val id: String,
) : Parcelable
