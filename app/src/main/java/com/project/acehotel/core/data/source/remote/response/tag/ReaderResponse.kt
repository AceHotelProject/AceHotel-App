package com.project.acehotel.core.data.source.remote.response.tag

import com.google.gson.annotations.SerializedName

data class ReaderResponse(

	@field:SerializedName("read_interval")
	val readInterval: Int? = null,

	@field:SerializedName("reader_name")
	val readerName: String? = null,

	@field:SerializedName("power_gain")
	val powerGain: Int? = null,

	@field:SerializedName("id")
	val id: String? = null
)
