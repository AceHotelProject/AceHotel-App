package com.project.acehotel.features.dashboard.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {
    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun getSelectedHotel() = hotelUseCase.getSelectedHotel().asLiveData()
}