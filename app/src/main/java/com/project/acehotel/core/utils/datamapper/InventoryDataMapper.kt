package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.model.InventoryHistory

object InventoryDataMapper {

    fun mapInventoryListResponseToDomain(input: InventoryListResponse): List<Inventory> =
        input.results!!.map { inventory ->
            Inventory(
                id = inventory?.id ?: "Empty",
                name = inventory?.name ?: "Empty",
                type = inventory?.type ?: "Empty",
                stock = inventory?.stock ?: -99,
                historyList = inventory?.inventoryUpdateHistory?.map { inventoryHistory ->
                    InventoryHistory(
                        historyId = inventoryHistory?.id ?: "Empty",
                        title = inventoryHistory?.title ?: "Empty",
                        desc = inventoryHistory?.description ?: "Empty",
                        stockChange = inventoryHistory?.stockChange ?: -99,
                        date = inventoryHistory?.date ?: "Empty"
                    )
                } ?: listOf()
            )
        }

    fun mapInventoryDetailResponseToDomain(input: InventoryDetailResponse): Inventory = Inventory(
        id = input.id ?: "Empty",
        name = input.name ?: "Empty",
        type = input.type ?: "Empty",
        stock = input.stock ?: -99,
        historyList = input.inventoryUpdateHistory?.map { inventoryHistory ->
            InventoryHistory(
                historyId = inventoryHistory?.id ?: "Empty",
                title = inventoryHistory?.title ?: "Empty",
                desc = inventoryHistory?.description ?: "Empty",
                stockChange = inventoryHistory?.stockChange ?: -99,
                date = inventoryHistory?.date ?: "Empty"
            )
        } ?: listOf()
    )
}