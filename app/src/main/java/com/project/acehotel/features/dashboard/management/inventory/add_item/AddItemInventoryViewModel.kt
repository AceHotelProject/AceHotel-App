package com.project.acehotel.features.dashboard.management.inventory.add_item

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
class AddItemInventoryViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private fun addInventory(
        hotelId: String,
        name: String,
        type: String,
        stock: Int
    ) = inventoryUseCase.addInventory(hotelId, name, type, stock).asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()


    fun executeAddInventory(
        name: String,
        type: String,
        stock: Int
    ): MediatorLiveData<Resource<Inventory>> =
        MediatorLiveData<Resource<Inventory>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(addInventory(hotel.id, name, type, stock)) { inventory ->
                    value = inventory
                }
            }
        }

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()
}