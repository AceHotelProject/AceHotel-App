package com.project.acehotel.features.dashboard.management.inventory.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryDetailViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    fun getDetailInventory(id: String, hotelId: String) =
        inventoryUseCase.getDetailInventory(id, hotelId).asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun getInventoryHistoryList(id: String, key: String) =
        inventoryUseCase.getInventoryHistoryList(id, key).asLiveData()
}