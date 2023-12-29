package com.project.acehotel.core.domain.inventory.interactor

import com.project.acehotel.core.data.repository.InventoryRespository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InventoryInteractor @Inject constructor(private val inventoryRepository: InventoryRespository) :
    InventoryUseCase {
    override fun getListInventory(token: String): Flow<Resource<List<Inventory>>> {
        return inventoryRepository.getListInventory(token)
    }

    override fun getDetailInventory(token: String, id: String): Flow<Resource<Inventory>> {
        return inventoryRepository.getDetailInventory(token, id)
    }

}