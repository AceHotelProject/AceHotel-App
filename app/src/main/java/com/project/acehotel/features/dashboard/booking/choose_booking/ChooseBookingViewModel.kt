package com.project.acehotel.features.dashboard.booking.choose_booking

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase

import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseBookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    fun getListBookingByHotel(
        hotelId: String, filterDate: String, visitorName: String = ""
    ) =
        bookingUseCase.getListBookingByHotel(hotelId, filterDate, visitorName).asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun executeGetListBookingToday(visitorName: String): MediatorLiveData<Resource<List<Booking>>> =
        MediatorLiveData<Resource<List<Booking>>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                val date = DateUtils.getDateThisDay()

                addSource(getListBookingByHotel(hotel.id, date, visitorName)) { booking ->
                    value = booking
                }
            }
        }

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()
}