package com.project.acehotel.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    fun getUser() = authUseCase.getUser().asLiveData()
}