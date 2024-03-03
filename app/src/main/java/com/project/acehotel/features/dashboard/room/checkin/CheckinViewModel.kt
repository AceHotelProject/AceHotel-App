package com.project.acehotel.features.dashboard.room.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.room.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckinViewModel @Inject constructor(private val roomUseCase: RoomUseCase) : ViewModel() {

    fun getRoomDetail(roomId: String) = roomUseCase.getRoomDetail(roomId).asLiveData()

    fun roomCheckin(
        roomId: String,
        checkinDate: String,
        bookingId: String,
        visitorId: String
    ) = roomUseCase.roomCheckin(roomId, checkinDate, bookingId, visitorId).asLiveData()
}