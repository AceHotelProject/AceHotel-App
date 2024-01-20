package com.project.acehotel.features.popup.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteItemViewModel @Inject constructor(private val inventoryUseCase: InventoryUseCase) :
    ViewModel() {

    fun deleteInventory(id: String) = inventoryUseCase.deleteInventory(id).asLiveData()
}