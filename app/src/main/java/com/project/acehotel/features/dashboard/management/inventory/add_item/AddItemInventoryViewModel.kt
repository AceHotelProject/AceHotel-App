package com.project.acehotel.features.dashboard.management.inventory.add_item

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
class AddItemInventoryViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val hotelUseCase: HotelUseCase,
) : ViewModel() {

    private fun addInventory(
        hotelId: String,
        name: String,
        type: String,
        stock: Int
    ) = inventoryUseCase.addInventory(hotelId, name, type, stock).asLiveData()

    private fun getSelectedHotel() = hotelUseCase.getSelectedHotel().asLiveData()

    fun executeAddInventory(
        name: String,
        type: String,
        stock: Int
    ): MediatorLiveData<Resource<Inventory>> =
        MediatorLiveData<Resource<Inventory>>().apply {
            addSource(getSelectedHotel()) { hotel ->
                addSource(addInventory(hotel, name, type, stock)) { inventory ->
                    value = inventory
                }
            }
        }
}