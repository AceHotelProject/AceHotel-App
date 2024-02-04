package com.project.acehotel.features.dashboard.management.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val authUseCase: AuthUseCase,
    private val hotelUseCase: HotelUseCase
) : ViewModel() {

    fun getListInventory(hotelId: String) = inventoryUseCase.getListInventory(hotelId).asLiveData()

    fun getSelectedHotel() = hotelUseCase.getSelectedHotel().asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()
}