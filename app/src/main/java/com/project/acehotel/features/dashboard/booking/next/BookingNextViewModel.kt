package com.project.acehotel.features.dashboard.booking.next

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingNextViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val hotelUseCase: HotelUseCase
) : ViewModel() {
    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    private fun getListBookingByHotel(hotelId: String, filterDate: String) =
        bookingUseCase.getListBookingByHotel(hotelId, filterDate)
            .asLiveData()

    fun executeGetListBookingByHotel(filterDate: String): MediatorLiveData<Resource<List<Booking>>> =
        MediatorLiveData<Resource<List<Booking>>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(getListBookingByHotel(hotel.id, filterDate)) { booking ->
                    value = booking
                }
            }
        }

    private fun getPagingListBookingByHotel(
        hotelId: String,
        filterDate: String,
        isFinished: Boolean
    ) =
        bookingUseCase.getPagingListBookingByHotel(hotelId, filterDate, isFinished)
            .asLiveData()

    fun executeGetPagingListBookingByHotel(
        filterDate: String,
        isFinished: Boolean
    ): MediatorLiveData<PagingData<Booking>> =
        MediatorLiveData<PagingData<Booking>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(
                    getPagingListBookingByHotel(
                        hotel.id,
                        filterDate,
                        isFinished
                    )
                ) { booking ->
                    value = booking
                }
            }
        }
}