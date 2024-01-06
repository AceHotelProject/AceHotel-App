package com.project.acehotel.core.data.source.remote.response.inventory

import com.google.gson.annotations.SerializedName

data class InventoryDetailResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

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
