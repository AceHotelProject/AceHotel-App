package com.project.acehotel.features.dashboard.management.inventory.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryDetailViewModel @Inject constructor(private val inventoryUseCase: InventoryUseCase) :
    ViewModel() {

    fun getDetailInventory(id: String) = inventoryUseCase.getDetailInventory(id).asLiveData()
}