package com.project.acehotel.core.data.source.remote.response.tag

import com.google.gson.annotations.SerializedName

data class AddTagResponse(

	@field:SerializedName("inventory_id")
	val inventoryId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tid")
	val tid: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
