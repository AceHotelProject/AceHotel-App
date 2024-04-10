package com.project.acehotel.core.data.source.remote.response.user

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("hotel_id")
	val hotelId: List<String?>? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("isEmailVerified")
	val isEmailVerified: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
