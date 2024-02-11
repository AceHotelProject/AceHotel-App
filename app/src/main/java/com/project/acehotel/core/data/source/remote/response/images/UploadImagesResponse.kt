package com.project.acehotel.core.data.source.remote.response.images

import com.google.gson.annotations.SerializedName

data class UploadImagesResponse(

	@field:SerializedName("path")
	val path: List<String?>? = null
)
