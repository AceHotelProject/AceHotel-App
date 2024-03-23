package com.project.acehotel.core.domain.booking.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomBooking(
    val id: String,
    val actualCheckin: String,
    val actualCheckout: String,
    val checkoutStaffId: String,
    val checkinStaffId: String,
    val hasProblem: Boolean,
    val note: String,
) : Parcelable