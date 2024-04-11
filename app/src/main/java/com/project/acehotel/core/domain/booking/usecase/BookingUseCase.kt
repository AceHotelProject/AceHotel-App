package com.project.acehotel.core.domain.booking.usecase

import androidx.paging.PagingData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.model.Note
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

    fun getListBookingByHotel(
        hotelId: String,
        filterDate: String,
        visitorName: String,
    ): Flow<Resource<List<Booking>>>

    fun getListBookingByRoom(roomId: String, filterDate: String): Flow<Resource<List<Booking>>>

    fun getListBookingByVisitor(visitorId: String): Flow<Resource<List<Booking>>>

    fun getDetailBooking(id: String): Flow<Resource<Booking>>

    fun deleteBooking(id: String): Flow<Resource<Int>>

    fun payBooking(
        id: String,
        transactionProof: String
    ): Flow<Resource<Booking>>

    fun applyDiscount(
        id: String,
        discountCode: String
    ): Flow<Resource<Booking>>

    fun getNoteDetail(
        id: String
    ): Flow<Resource<Note>>
    // TEST PAGING

    fun getPagingListBookingByHotel(
        hotelId: String,
        filterDate: String,
        isFinished: Boolean,
        visitorName: String
    ): Flow<PagingData<Booking>>

    fun getPagingListBookingByVisitor(
        visitorId: String,
        filterDate: String,
        isFinished: Boolean
    ): Flow<PagingData<Booking>>

    fun getPagingListBookingByRoom(
        roomId: String,
        filterDate: String,
        isFinished: Boolean
    ): Flow<PagingData<Booking>>

    // TEST PAGING
}