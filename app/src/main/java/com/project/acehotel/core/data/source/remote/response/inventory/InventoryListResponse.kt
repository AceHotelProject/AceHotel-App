package com.project.acehotel.core.data.source.remote.response.inventory

import com.google.gson.annotations.SerializedName

data class InventoryListResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("totalResults")
    val totalResults: Int? = null,

    @field:SerializedName("limit")
    val limit: Int? = null,

    @field:SerializedName("totalPages")
    val totalPages: Int? = null,

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("results")
    val results: List<InventoryResultsItem?>? = null
)

data class InventoryUpdateHistoryItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("stockChange")
    val stockChange: Int? = null
)

data class InventoryResultsItem(

    @field:SerializedName("inventory_update_history")
    val inventoryUpdateHistory: List<InventoryUpdateHistoryItem?>? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("stock")
    val stock: Int? = null
)
