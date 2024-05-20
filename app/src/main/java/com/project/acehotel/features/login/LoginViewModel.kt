package com.project.acehotel.features.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.utils.datamapper.HotelDataMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val hotelUseCase: HotelUseCase,
) : ViewModel() {

    fun getUser() = authUseCase.getUser().asLiveData()

    fun loginUser(email: String, password: String) =
        authUseCase.loginUser(email, password).asLiveData()

    fun insertCacheUser(user: Auth) = viewModelScope.launch {
        authUseCase.insertCacheUser(user)
    }

    private fun saveAccessToken(token: String) = authUseCase.saveAccessToken(token).asLiveData()

    private fun saveRefreshToken(token: String) = authUseCase.saveRefreshToken(token).asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun saveSelectedHotelData(data: ManageHotel) =
        hotelUseCase.saveSelectedHotelData(data).asLiveData()

    fun executeValidateToken(refreshToken: String, accessToken: String): MediatorLiveData<String> =
        MediatorLiveData<String>().apply {
            addSource(saveRefreshToken(refreshToken)) { refreshToken ->
                addSource(saveAccessToken(accessToken)) { accessToken ->

                    if (refreshToken && accessToken) {
                        addSource(getRefreshToken()) { token ->
                            value = token
                        }
                    }
                }
            }
        }

    fun getHotel(id: String) = hotelUseCase.getHotel(id).asLiveData()

    fun executeSaveCurrentHotel(id: String): MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            addSource(getHotel(id)) { hotel ->
                when (hotel) {
                    is Resource.Error -> {
                        value = false
                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Message -> {
                        value = false
                    }

                    is Resource.Success -> {
                        val data = HotelDataMapper.mapHotelToManageHotel(hotel.data)
                        addSource(saveSelectedHotelData(data)) {
                            value = it
                        }
                    }
                }
            }
        }
}