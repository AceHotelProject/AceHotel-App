package com.project.acehotel.features.dashboard.room.detail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.room.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val roomUseCase: RoomUseCase,
    private val hotelUseCase: HotelUseCase,
    private val bookingUseCase: BookingUseCase,
) : ViewModel() {

    fun getRoomDetail(roomId: String) = roomUseCase.getRoomDetail(roomId).asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun getListBookingByRoom(roomId: String, filterDate: String) =
        bookingUseCase.getListBookingByRoom(roomId, filterDate).asLiveData()

    private fun getListBookingByHotel(
        hotelId: String,
        filterDate: String,
        visitorName: String = ""
    ) =
        bookingUseCase.getListBookingByHotel(hotelId, filterDate, visitorName)
            .asLiveData()

    fun executeGetListBookingByHotel(filterDate: String): MediatorLiveData<Resource<List<Booking>>> =
        MediatorLiveData<Resource<List<Booking>>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(getListBookingByHotel(hotel.id, filterDate)) { booking ->
                    value = booking
                }
            }
        }
}