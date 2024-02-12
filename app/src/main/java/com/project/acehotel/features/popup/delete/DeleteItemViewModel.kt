package com.project.acehotel.features.popup.delete

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteItemViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val hotelUseCase: HotelUseCase
) :
    ViewModel() {

    fun getSelectedHotel() = hotelUseCase.getSelectedHotel().asLiveData()

    private fun deleteInventory(id: String, hotelId: String) =
        inventoryUseCase.deleteInventory(id, hotelId).asLiveData()

    fun executeDeleteInventory(id: String): MediatorLiveData<Resource<Int>> =
        MediatorLiveData<Resource<Int>>().apply {
            addSource(getSelectedHotel()) { hotel ->
                addSource(deleteInventory(id, hotel)) { hotel ->
                    value = hotel
                }
            }
        }
}