package com.project.acehotel.core.domain.inventory.usecase

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import kotlinx.coroutines.flow.Flow

interface InventoryUseCase {

    fun getListInventory(): Flow<Resource<List<Inventory>>>

    fun getDetailInventory(id: String): Flow<Resource<Inventory>>
}