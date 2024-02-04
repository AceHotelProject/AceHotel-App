package com.project.acehotel.core.domain.inventory.usecase

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import kotlinx.coroutines.flow.Flow

interface InventoryUseCase {

    fun getListInventory(hotelId: String): Flow<Resource<List<Inventory>>>

    fun getDetailInventory(id: String, hotelId: String): Flow<Resource<Inventory>>

    fun addInventory(
        name: String,
        type: String,
        stock: Int
    ): Flow<Resource<Inventory>>

    fun updateInventory(
        id: String,
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ): Flow<Resource<Inventory>>

    fun deleteInventory(
        id: String
    ): Flow<Resource<Int>>
}