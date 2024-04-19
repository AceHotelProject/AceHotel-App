package com.project.acehotel.features.dashboard.profile.update_user

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
class UpdateUserViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val hotelUseCase: HotelUseCase
) : ViewModel() {

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun updateUser(
        id: String,
        hotelId: String,
        email: String,
    ) = authUseCase.updateUser(id, hotelId, email)
        .asLiveData()

    fun executeUpdateUser(
        id: String, email: String,
        username: String,
        role: String,
    ): MediatorLiveData<Resource<User>> = MediatorLiveData<Resource<User>>().apply {
        addSource(getSelectedHotelData()) { hotel ->
            addSource(updateUser(id, hotel.id, email)) { user ->
                value = user
            }
        }
    }
}