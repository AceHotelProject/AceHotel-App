package com.project.acehotel.features.dashboard.management.inventory.choose_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseItemViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    fun getListInventory(hotelId: String) = inventoryUseCase.getListInventory(hotelId).asLiveData()

    fun getSelectedHotel() = hotelUseCase.getSelectedHotel().asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()
}