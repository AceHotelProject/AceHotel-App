package com.project.acehotel.features.dashboard.management.inventory.change_stock

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeStockItemInventoryViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val hotelUseCase: HotelUseCase,
) : ViewModel() {

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    private fun updateInventory(
        id: String,
        hotelId: String,
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ) = inventoryUseCase.updateInventory(id, hotelId, name, type, stock, title, description)
        .asLiveData()

    fun executeUpdateInventory(
        id: String,
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ): MediatorLiveData<Resource<Inventory>> = MediatorLiveData<Resource<Inventory>>().apply {
        addSource(getSelectedHotelData()) { hotel ->
            addSource(
                updateInventory(
                    id,
                    hotel.id,
                    name,
                    type,
                    stock,
                    title,
                    description
                )
            ) { inventory ->
                value = inventory
            }
        }
    }
}