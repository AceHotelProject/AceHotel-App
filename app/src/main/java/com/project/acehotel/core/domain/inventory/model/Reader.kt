package com.project.acehotel.core.domain.inventory.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reader(
    val id: String,
    val powerGain: Int,
    val readInterval: Int,
    val readerName: String,
    val isActive: Boolean
) : Parcelable