package com.project.acehotel.features.dashboard.room

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.room.model.Room
import com.project.acehotel.core.domain.room.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val hotelUseCase: HotelUseCase,
    private val roomUseCase: RoomUseCase,
) : ViewModel() {

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    private fun getListRoomByHotel(hotelId: String) =
        roomUseCase.getListRoomByHotel(hotelId).asLiveData()

    fun executeGetListRoomByHotel(): MediatorLiveData<Resource<List<Room>>> =
        MediatorLiveData<Resource<List<Room>>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(getListRoomByHotel(hotel.id)) { room ->
                    value = room
                }
            }
        }
}