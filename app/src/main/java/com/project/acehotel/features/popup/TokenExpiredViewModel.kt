package com.project.acehotel.features.popup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TokenExpiredViewModel @Inject constructor(private val authUseCase: AuthUseCase) :
    ViewModel() {

    fun deleteToken() = viewModelScope.launch {
        authUseCase.deleteToken()
    }
}