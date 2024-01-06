package com.project.acehotel.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    fun loginUser(email: String, password: String) =
        authUseCase.loginUser(email, password).asLiveData()

    fun insertCacheUser(user: Auth) = viewModelScope.launch {
        authUseCase.insertCacheUser(user)
    }

    fun saveAccessToken(token: String) = viewModelScope.launch {
        authUseCase.saveAccessToken(token)
    }

    fun saveRefreshToken(token: String) = viewModelScope.launch {
        authUseCase.saveRefreshToken(token)
    }
}