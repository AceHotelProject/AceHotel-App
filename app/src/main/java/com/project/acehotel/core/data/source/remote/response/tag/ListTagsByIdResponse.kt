package com.project.acehotel.core.data.source.remote.response.tag

import com.google.gson.annotations.SerializedName

data class ListTagsByIdResponse(

	@field:SerializedName("tagId")
	val tagId: List<String?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)
