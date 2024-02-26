package com.project.acehotel.features.popup.delete

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteItemViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val hotelUseCase: HotelUseCase,
    private val visitorUseCase: VisitorUseCase,
) :
    ViewModel() {

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun saveSelectedHotelData(data: ManageHotel) = viewModelScope.launch {
        hotelUseCase.saveSelectedHotelData(data)
    }

    private fun deleteInventory(id: String, hotelId: String) =
        inventoryUseCase.deleteInventory(id, hotelId).asLiveData()

    fun executeDeleteInventory(id: String): MediatorLiveData<Resource<Int>> =
        MediatorLiveData<Resource<Int>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(deleteInventory(id, hotel.id)) { hotel ->
                    value = hotel
                }
            }
        }

    fun executeDeleteHotel(id: String) = hotelUseCase.deleteHotel(id).asLiveData()

    private fun deleteVisitor(id: String, hotelId: String) =
        visitorUseCase.deleteVisitor(id, hotelId).asLiveData()

    fun executeDeleteVisitor(id: String): MediatorLiveData<Resource<Int>> =
        MediatorLiveData<Resource<Int>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(deleteVisitor(id, hotel.id)) { visitor ->
                    value = visitor
                }
            }
        }
}