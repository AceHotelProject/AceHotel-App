package com.project.acehotel.core.domain.inventory.repository

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.model.InventoryHistory
import kotlinx.coroutines.flow.Flow

interface IInventoryIRepository {

    fun getListInventory(
        hotelId: String,
        name: String,
        type: String
    ): Flow<Resource<List<Inventory>>>

    fun getInventoryHistoryList(
        id: String,
        key: String,
    ): Flow<Resource<List<InventoryHistory>>>

    fun getDetailInventory(id: String, hotelId: String): Flow<Resource<Inventory>>

    fun addInventory(
        hotelId: String,
        name: String,
        type: String,
        stock: Int
    ): Flow<Resource<Inventory>>

    fun updateInventory(
        id: String,
        hotelId: String,
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ): Flow<Resource<Inventory>>

    fun deleteInventory(
        id: String,
        hotelId: String,
    ): Flow<Resource<Int>>
}