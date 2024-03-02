package com.project.acehotel.features.dashboard.booking.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingDetailViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val hotelUseCase: HotelUseCase,
    private val visitorUseCase: VisitorUseCase,
) : ViewModel() {

    fun getDetailBooking(id: String) = bookingUseCase.getDetailBooking(id).asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun getVisitorDetail(id: String) = visitorUseCase.getVisitorDetail(id).asLiveData()
}