package com.project.acehotel.features.dashboard.management.visitor.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisitorDetailViewModel @Inject constructor(
    private val visitorUseCase: VisitorUseCase,
    private val hotelUseCase: HotelUseCase,
    private val bookingUseCase: BookingUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    fun getUser() = authUseCase.getUser().asLiveData()

    private fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun getPagingListBookingByVisitor(
        visitorId: String,
        filterDate: String,
        isFinished: Boolean
    ) = bookingUseCase.getPagingListBookingByVisitor(visitorId, filterDate, isFinished).asLiveData()

    fun getVisitorDetail(id: String) = visitorUseCase.getVisitorDetail(id).asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()
}