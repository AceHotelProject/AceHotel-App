package com.project.acehotel.core.domain.inventory.repository

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import kotlinx.coroutines.flow.Flow

interface IInventoryIRepository {

    fun getListInventory(): Flow<Resource<List<Inventory>>>

    fun getDetailInventory(id: String): Flow<Resource<Inventory>>
}