package com.project.acehotel.core.domain.inventory.interactor

import com.project.acehotel.core.data.repository.InventoryRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InventoryInteractor @Inject constructor(private val inventoryRepository: InventoryRepository) :
    InventoryUseCase {
    override fun getListInventory(): Flow<Resource<List<Inventory>>> {
        return inventoryRepository.getListInventory()
    }

    override fun getDetailInventory(id: String): Flow<Resource<Inventory>> {
        return inventoryRepository.getDetailInventory(id)
    }

    override fun addInventory(name: String, type: String, stock: Int): Flow<Resource<Inventory>> {
        return inventoryRepository.addInventory(name, type, stock)
    }

    override fun updateInventory(
        id: String,
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ): Flow<Resource<Inventory>> {
        return inventoryRepository.updateInventory(id, name, type, stock, title, description)
    }

    override fun deleteInventory(id: String): Flow<Resource<Int>> {
        return inventoryRepository.deleteInventory(id)
    }


}