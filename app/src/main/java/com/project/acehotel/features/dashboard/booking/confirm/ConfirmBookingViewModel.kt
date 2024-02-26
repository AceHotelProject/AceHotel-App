package com.project.acehotel.features.dashboard.booking.confirm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfirmBookingViewModel @Inject constructor(
    private val hotelUseCase: HotelUseCase,
    private val bookingUseCase: BookingUseCase,
    private val visitorUseCase: VisitorUseCase,
) : ViewModel() {

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    private fun addBooking(
        hotelId: String,
        visitorId: String,
        checkinDate: String,
        duration: Int,
        roomCount: Int,
        extraBed: Int,
        type: String,
    ) = bookingUseCase.addBooking(
        hotelId,
        visitorId,
        checkinDate,
        duration,
        roomCount,
        extraBed,
        type
    ).asLiveData()

    fun executeAddBooking(
        visitorId: String,
        checkinDate: String,
        duration: Int,
        roomCount: Int,
        extraBed: Int,
        type: String,
    ): MediatorLiveData<Resource<Booking>> = MediatorLiveData<Resource<Booking>>().apply {
        addSource(getSelectedHotelData()) { hotel ->
            addSource(
                addBooking(
                    hotel.id,
                    visitorId,
                    checkinDate,
                    duration,
                    roomCount,
                    extraBed,
                    type
                )
            ) { booking ->
                value = booking
            }
        }
    }

    fun getVisitorDetail(id: String) = visitorUseCase.getVisitorDetail(id).asLiveData()
}