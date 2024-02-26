package com.project.acehotel.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val hotelUseCase: HotelUseCase
) : ViewModel() {

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()
}