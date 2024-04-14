package com.project.acehotel.core.domain.inventory.interactor

import com.project.acehotel.core.data.repository.InventoryRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.model.InventoryHistory
import com.project.acehotel.core.domain.inventory.model.Reader
import com.project.acehotel.core.domain.inventory.model.Tag
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InventoryInteractor @Inject constructor(private val inventoryRepository: InventoryRepository) :
    InventoryUseCase {
    override fun getListInventory(
        hotelId: String,
        name: String,
        type: String
    ): Flow<Resource<List<Inventory>>> {
        return inventoryRepository.getListInventory(hotelId, name, type)
    }

    override fun getInventoryHistoryList(
        id: String,
        key: String
    ): Flow<Resource<List<InventoryHistory>>> {
        return inventoryRepository.getInventoryHistoryList(id, key)
    }

    override fun getDetailInventory(id: String, hotelId: String): Flow<Resource<Inventory>> {
        return inventoryRepository.getDetailInventory(id, hotelId)
    }

    override fun addInventory(
        hotelId: String,
        name: String,
        type: String,
        stock: Int
    ): Flow<Resource<Inventory>> {
        return inventoryRepository.addInventory(hotelId, name, type, stock)
    }

    override fun updateInventory(
        id: String,
        hotelId: String,
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ): Flow<Resource<Inventory>> {
        return inventoryRepository.updateInventory(
            id,
            hotelId,
            name,
            type,
            stock,
            title,
            description
        )
    }

    override fun deleteInventory(id: String, hotelId: String): Flow<Resource<Int>> {
        return inventoryRepository.deleteInventory(id, hotelId)
    }

    override fun getTagById(readerId: String): Flow<Resource<String>> {
        return inventoryRepository.getTagById(readerId)
    }

    override fun getTag(): Flow<Resource<List<Tag>>> {
        return inventoryRepository.getTag()
    }

    override fun addTag(tid: String, inventoryId: String): Flow<Resource<Tag>> {
        return inventoryRepository.addTag(tid, inventoryId)
    }

    override fun updateReader(
        readerId: String,
        powerGain: Int,
        readInterval: Int
    ): Flow<Resource<Reader>> {
        return inventoryRepository.updateReader(readerId, powerGain, readInterval)
    }

    override fun getReader(readerId: String): Flow<Resource<Reader>> {
        return inventoryRepository.getReader(readerId)
    }

    override fun setQueryTag(readerId: String, state: Boolean): Flow<Resource<Boolean>> {
        return inventoryRepository.setQueryTag(readerId, state)
    }
}