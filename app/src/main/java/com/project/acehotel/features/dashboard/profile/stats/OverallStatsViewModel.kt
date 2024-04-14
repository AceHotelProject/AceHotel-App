package com.project.acehotel.features.dashboard.profile.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverallStatsViewModel @Inject constructor(
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    fun getHotelRecap(filterDate: String) = hotelUseCase.getHotelRecap(filterDate).asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()
}