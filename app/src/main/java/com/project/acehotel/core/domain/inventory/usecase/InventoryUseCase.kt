package com.project.acehotel.core.domain.inventory.usecase

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.model.InventoryHistory
import com.project.acehotel.core.domain.inventory.model.Reader
import com.project.acehotel.core.domain.inventory.model.Tag
import kotlinx.coroutines.flow.Flow

interface InventoryUseCase {

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

    fun getTagById(readerId: String): Flow<Resource<String>>

    fun getTag(): Flow<Resource<List<Tag>>>

    fun addTag(
        tid: String,
        inventoryId: String,
    ): Flow<Resource<Tag>>

    fun updateReader(
        readerId: String,
        powerGain: Int,
        readInterval: Int
    ): Flow<Resource<Reader>>

    fun getReader(
        readerId: String,
    ): Flow<Resource<Reader>>

    fun setQueryTag(
        readerId: String,
        state: Boolean,
    ): Flow<Resource<Boolean>>
}