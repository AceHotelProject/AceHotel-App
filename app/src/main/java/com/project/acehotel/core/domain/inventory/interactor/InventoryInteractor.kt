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

    override fun getDetailInventory(token: String, id: String): Flow<Resource<Inventory>> {
        return inventoryRepository.getDetailInventory(token, id)
    }

}