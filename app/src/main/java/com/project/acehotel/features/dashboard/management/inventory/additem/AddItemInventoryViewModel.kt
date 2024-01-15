package com.project.acehotel.features.dashboard.management.inventory.additem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddItemInventoryViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase
) : ViewModel() {

    fun addInventory(
        name: String,
        type: String,
        stock: Int
    ) = inventoryUseCase.addInventory(name, type, stock).asLiveData()
}