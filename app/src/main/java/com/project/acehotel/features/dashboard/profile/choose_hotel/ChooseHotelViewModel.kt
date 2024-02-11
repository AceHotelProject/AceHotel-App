package com.project.acehotel.features.dashboard.profile.choose_hotel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseHotelViewModel @Inject constructor(
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    fun getHotels() = hotelUseCase.getListHotel().asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun getSelectedHotel() = hotelUseCase.getSelectedHotel().asLiveData()

    fun saveSelectedHotel(id: String) = viewModelScope.launch {
        hotelUseCase.saveSelectedHotel(id)
    }
}