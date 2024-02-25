package com.project.acehotel.core.domain.booking.repository

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import kotlinx.coroutines.flow.Flow

interface IBookingRepository {

    fun addBooking(
        hotelId: String,
        visitorId: String,
        checkinDate: String,
        duration: Int,
        roomCount: Int,
        extraBed: Int,
        type: String,
    ): Flow<Resource<Booking>>

    fun deleteBooking(id: String): Flow<Resource<Int>>
}