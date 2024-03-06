package com.project.acehotel.core.domain.booking.interactor

import com.project.acehotel.core.data.repository.BookingRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
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
        filterDate: String
    ): Flow<Resource<List<Booking>>> {
        return bookingRepository.getListBookingByHotel(hotelId, filterDate)
    }

    override fun getListBookingByRoom(roomId: String): Flow<Resource<List<Booking>>> {
        return bookingRepository.getListBookingByRoom(roomId)
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
}