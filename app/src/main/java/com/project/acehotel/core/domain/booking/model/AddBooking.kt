package com.project.acehotel.core.domain.booking.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddBooking(
    val hotelId: String,
    val visitorId: String,
    val checkinDate: String,
    val type: String,
    val duration: Int,
    val roomCount: Int,
    val extraBed: Int,
) : Parcelable
