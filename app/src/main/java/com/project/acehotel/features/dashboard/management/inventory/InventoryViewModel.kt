package com.project.acehotel.features.dashboard.management.inventory

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val authUseCase: AuthUseCase,
    private val hotelUseCase: HotelUseCase
) : ViewModel() {

    private fun getListInventory(hotelId: String, name: String, type: String) =
        inventoryUseCase.getListInventory(hotelId, name, type).asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun fetchListInventory(
        name: String,
        type: String
    ): MediatorLiveData<Resource<List<Inventory>>> =
        MediatorLiveData<Resource<List<Inventory>>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(getListInventory(hotel.id, name, type)) { inventory ->
                    value = inventory
                }
            }
        }
}