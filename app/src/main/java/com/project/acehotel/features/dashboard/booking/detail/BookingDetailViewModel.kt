package com.project.acehotel.features.dashboard.booking.detail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.room.usecase.RoomUseCase
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingDetailViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val hotelUseCase: HotelUseCase,
    private val visitorUseCase: VisitorUseCase,
    private val roomUseCase: RoomUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {
    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun getVisitorDetail(id: String) = visitorUseCase.getVisitorDetail(id).asLiveData()

    fun getRoomDetail(roomId: String) = roomUseCase.getRoomDetail(roomId).asLiveData()

    private fun getUserById(id: String, hotelId: String) =
        authUseCase.getUserById(id, hotelId).asLiveData()

    fun executeGetUserById(id: String): MediatorLiveData<Resource<User>> =
        MediatorLiveData<Resource<User>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(getUserById(id, hotel.id)) { user ->
                    value = user
                }
            }
        }
}