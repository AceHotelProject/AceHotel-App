package com.project.acehotel.features.dashboard.management.finance

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    private fun getPagingListBookingByHotel(
        hotelId: String,
        filterDate: String,
        isFinished: Boolean,
        visitorName: String,
    ) =
        bookingUseCase.getPagingListBookingByHotel(hotelId, filterDate, isFinished, visitorName)
            .asLiveData()

    fun executeGetPagingListBookingByHotel(
        filterDate: String,
        isFinished: Boolean,
        visitorName: String,
    ): MediatorLiveData<PagingData<Booking>> =
        MediatorLiveData<PagingData<Booking>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(
                    getPagingListBookingByHotel(
                        hotel.id,
                        filterDate,
                        isFinished,
                        visitorName
                    )
                ) { booking ->
                    value = booking
                }
            }
        }

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
                addSource(getListBookingByHotel(hotel.id, filterDate)) { booking ->
                    value = booking
                }
            }
        }

    fun getUser() = authUseCase.getUser().asLiveData()
}