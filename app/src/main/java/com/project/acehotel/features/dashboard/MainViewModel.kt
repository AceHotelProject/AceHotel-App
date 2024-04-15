package com.project.acehotel.features.dashboard

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val hotelUseCase: HotelUseCase,
    private val bookingUseCase: BookingUseCase
) : ViewModel() {

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun getListBookingByHotel(
        hotelId: String,
        filterDate: String,
        visitorName: String = "",
    ) = bookingUseCase.getListBookingByHotel(hotelId, filterDate, visitorName).asLiveData()

    fun executeGetListBookingByHotel(
        filterDate: String,
    ): MediatorLiveData<Resource<List<Booking>>> =
        MediatorLiveData<Resource<List<Booking>>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(
                    getListBookingByHotel(
                        hotel.id, filterDate
                    )
                ) { booking ->
                    value = booking
                }
            }
        }
}