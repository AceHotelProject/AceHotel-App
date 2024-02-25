package com.project.acehotel.features.dashboard.booking.add_booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBookingViewModel @Inject constructor(
    private val hotelUseCase: HotelUseCase,
    private val bookingUseCase: BookingUseCase
) : ViewModel() {

    fun getSelectedHotel() = hotelUseCase.getSelectedHotel().asLiveData()

//    fun addBooking(
//        hotelId: String,
//        visitorId: String,
//        checkinDate: String,
//        duration: Int,
//        roomCount: Int,
//        extraBed: Int,
//        type: String,
//    ) = bookingUseCase.addBooking(
//        hotelId,
//        visitorId,
//        checkinDate,
//        duration,
//        roomCount,
//        extraBed,
//        type
//    ).asLiveData()
//
//    fun executeAddBooking(
//        visitorId: String,
//        checkinDate: String,
//        duration: Int,
//        roomCount: Int,
//        extraBed: Int,
//        type: String,
//    ): MediatorLiveData<Resource<Booking>> = MediatorLiveData<Resource<Booking>>().apply {
//        addSource(getSelectedHotel()) { hotelId ->
//            addSource(
//                addBooking(
//                    hotelId,
//                    visitorId,
//                    checkinDate,
//                    duration,
//                    roomCount,
//                    extraBed,
//                    type
//                )
//            ) { booking ->
//                value = booking
//            }
//        }
//    }
}