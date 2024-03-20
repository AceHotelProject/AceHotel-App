package com.project.acehotel.core.domain.hotel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HotelRecap(
    val revenue: Int,
    val branchCount: Int,
    val checkinCount: Int,
    val totalBooking: Int,
) : Parcelable
