package com.project.acehotel.core.domain.booking.repository

import androidx.paging.PagingData
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

    fun getListBookingByHotel(hotelId: String, filterDate: String): Flow<Resource<List<Booking>>>

    fun getListBookingByRoom(roomId: String): Flow<Resource<List<Booking>>>

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

    // TEST PAGING

    fun getPagingListBookingByHotel(
        hotelId: String,
        filterDate: String
    ): Flow<PagingData<Booking>>

    // TEST PAGING
}