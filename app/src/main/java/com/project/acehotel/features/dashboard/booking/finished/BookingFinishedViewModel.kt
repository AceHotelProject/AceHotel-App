package com.project.acehotel.features.dashboard.booking.finished

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingFinishedViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val hotelUseCase: HotelUseCase
) : ViewModel() {
    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    private fun getPagingListBookingByHotel(hotelId: String, filterDate: String) =
        bookingUseCase.getPagingListBookingByHotel(hotelId, filterDate)
            .asLiveData()

    fun executeGetPagingListBookingByHotel(filterDate: String): MediatorLiveData<PagingData<Booking>> =
        MediatorLiveData<PagingData<Booking>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(getPagingListBookingByHotel(hotel.id, filterDate)) { booking ->
                    value = booking
                }
            }
        }


}