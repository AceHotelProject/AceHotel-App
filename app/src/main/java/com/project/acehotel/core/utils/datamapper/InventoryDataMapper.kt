package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryUpdateHistoryItem
import com.project.acehotel.core.data.source.remote.response.tag.AddTagResponse
import com.project.acehotel.core.data.source.remote.response.tag.ListTagsResponse
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.model.InventoryHistory
import com.project.acehotel.core.domain.inventory.model.Tag

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
                        date = inventoryHistory?.date ?: "Empty",
                        personInCharge = inventoryHistory?.personInCharge ?: "Empty",
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
                date = inventoryHistory?.date ?: "Empty",
                personInCharge = inventoryHistory?.personInCharge ?: "Empty",
            )
        } ?: listOf()
    )

    fun mapListInventoryHistoryToDomain(input: List<InventoryUpdateHistoryItem?>?): List<InventoryHistory> =
        input?.map { inventoryHistory ->
            InventoryHistory(
                historyId = inventoryHistory?.id ?: "Empty",
                title = inventoryHistory?.title ?: "Empty",
                desc = inventoryHistory?.description ?: "Empty",
                stockChange = inventoryHistory?.stockChange ?: 0,
                date = inventoryHistory?.date ?: "Empty",
                personInCharge = inventoryHistory?.personInCharge ?: "Empty",
            )
        } ?: listOf()

    fun mapAddTagResponseToDomain(input: AddTagResponse): Tag = Tag(
        status = input.status ?: "Empty",
        tid = input.tid ?: "Empty",
        inventoryId = input.inventoryId ?: "Empty",
        id = input.id ?: "Empty",
    )

    fun mapListTagResponseToDomain(input: ListTagsResponse): List<Tag> = input.results?.map { tag ->
        Tag(
            status = tag?.status ?: "Empty",
            tid = tag?.tid ?: "Empty",
            inventoryId = tag?.inventoryId ?: "Empty",
            id = tag?.id ?: "Empty",
        )
    } ?: listOf()
}