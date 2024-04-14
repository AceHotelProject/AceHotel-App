package com.project.acehotel.features.dashboard.profile.update_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateUserViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()
}