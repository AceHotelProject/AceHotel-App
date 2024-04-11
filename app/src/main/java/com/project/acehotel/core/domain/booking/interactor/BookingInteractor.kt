package com.project.acehotel.core.domain.booking.interactor

import androidx.paging.PagingData
import com.project.acehotel.core.data.repository.BookingRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.model.Note
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookingInteractor @Inject constructor(private val bookingRepository: BookingRepository) :
    BookingUseCase {
    override fun addBooking(
        hotelId: String,
        visitorId: String,
        checkinDate: String,
        duration: Int,
        roomCount: Int,
        extraBed: Int,
        type: String
    ): Flow<Resource<Booking>> {
        return bookingRepository.addBooking(
            hotelId,
            visitorId,
            checkinDate,
            duration,
            roomCount,
            extraBed,
            type
        )
    }

    override fun getListBookingByHotel(
        hotelId: String,
        filterDate: String,
        visitorName: String
    ): Flow<Resource<List<Booking>>> {
        return bookingRepository.getListBookingByHotel(hotelId, filterDate, visitorName)
    }

    override fun getListBookingByRoom(
        roomId: String,
        filterDate: String
    ): Flow<Resource<List<Booking>>> {
        return bookingRepository.getListBookingByRoom(roomId, filterDate)
    }

    override fun getListBookingByVisitor(visitorId: String): Flow<Resource<List<Booking>>> {
        return bookingRepository.getListBookingByVisitor(visitorId)
    }

    override fun getDetailBooking(id: String): Flow<Resource<Booking>> {
        return bookingRepository.getDetailBooking(id)
    }


    override fun deleteBooking(id: String): Flow<Resource<Int>> {
        return bookingRepository.deleteBooking(id)
    }

    override fun payBooking(id: String, transactionProof: String): Flow<Resource<Booking>> {
        return bookingRepository.payBooking(id, transactionProof)
    }

    override fun applyDiscount(id: String, discountCode: String): Flow<Resource<Booking>> {
        return bookingRepository.applyDiscount(id, discountCode)
    }

    override fun getNoteDetail(id: String): Flow<Resource<Note>> {
        return bookingRepository.getNoteDetail(id)
    }

    override fun getPagingListBookingByHotel(
        hotelId: String,
        filterDate: String,
        isFinished: Boolean,
        visitorName: String
    ): Flow<PagingData<Booking>> {
        return bookingRepository.getPagingListBookingByHotel(
            hotelId,
            filterDate,
            isFinished,
            visitorName
        )
    }

    override fun getPagingListBookingByVisitor(
        visitorId: String,
        filterDate: String,
        isFinished: Boolean
    ): Flow<PagingData<Booking>> {
        return bookingRepository.getPagingListBookingByVisitor(visitorId, filterDate, isFinished)
    }

    override fun getPagingListBookingByRoom(
        roomId: String,
        filterDate: String,
        isFinished: Boolean
    ): Flow<PagingData<Booking>> {
        return bookingRepository.getPagingListBookingByRoom(roomId, filterDate, isFinished)
    }
}