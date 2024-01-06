package com.project.acehotel.core.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("access")
	val access: Access? = null,

	@field:SerializedName("refresh")
	val refresh: Refresh? = null
)
