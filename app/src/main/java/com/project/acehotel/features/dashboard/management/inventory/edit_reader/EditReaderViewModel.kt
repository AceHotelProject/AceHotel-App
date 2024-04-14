package com.project.acehotel.features.dashboard.management.inventory.edit_reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditReaderViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    fun updateReader(
        readerId: String,
        powerGain: Int,
        readInterval: Int
    ) = inventoryUseCase.updateReader(readerId, powerGain, readInterval).asLiveData()

    fun setQueryTag(
        readerId: String,
        powerGain: Boolean,
    ) = inventoryUseCase.setQueryTag(readerId, powerGain).asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()
}