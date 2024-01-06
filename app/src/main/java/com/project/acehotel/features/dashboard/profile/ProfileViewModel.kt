package com.project.acehotel.features.dashboard.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    fun getUser() = authUseCase.getUser().asLiveData()

    fun deleteUser(user: Auth) = viewModelScope.launch {
        authUseCase.deleteUser(user)
    }

    fun deleteToken() = viewModelScope.launch {
        authUseCase.deleteToken()
    }

}