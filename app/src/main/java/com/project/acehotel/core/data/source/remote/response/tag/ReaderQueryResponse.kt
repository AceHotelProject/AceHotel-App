package com.project.acehotel.core.data.source.remote.response.tag

import com.google.gson.annotations.SerializedName

data class ReaderQueryResponse(

	@field:SerializedName("method")
	val method: String? = null,

	@field:SerializedName("params")
	val params: String? = null
)
