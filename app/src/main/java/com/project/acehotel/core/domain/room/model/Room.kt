package com.project.acehotel.core.domain.room.model

import android.os.Parcelable
import com.project.acehotel.core.domain.booking.model.Booking
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    val facility: Facility,
    val type: String,
    val isBooked: Boolean,
    val isClean: Boolean,
    val hotelId: String,
    val price: Int,
    val bookings: List<Booking>,
    val id: String
) : Parcelable
