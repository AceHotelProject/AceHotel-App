package com.project.acehotel.features.dashboard.management.inventory.change_stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeStockItemInventoryViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase
) : ViewModel() {

    fun updateInventory(
        id: String,
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ) = inventoryUseCase.updateInventory(id, name, type, stock, title, description).asLiveData()
}