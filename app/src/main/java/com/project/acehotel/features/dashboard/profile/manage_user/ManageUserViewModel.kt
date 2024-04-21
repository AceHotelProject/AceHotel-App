package com.project.acehotel.features.dashboard.profile.manage_user

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageUserViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val hotelUseCase: HotelUseCase,
) : ViewModel() {

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    private fun getUserByHotel(hotelId: String) = authUseCase.getUserByHotel(hotelId).asLiveData()

    fun executeGetUserByHotel(): MediatorLiveData<Resource<List<User>>> =
        MediatorLiveData<Resource<List<User>>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(getUserByHotel(hotel.id)) { user ->
                    value = user
                }
            }
        }

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun getUser() = authUseCase.getUser().asLiveData()
}