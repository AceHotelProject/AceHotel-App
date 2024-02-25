package com.project.acehotel.core.domain.booking.usecase

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import kotlinx.coroutines.flow.Flow

interface BookingUseCase {

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