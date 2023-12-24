package com.project.acehotel.core.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("tokens")
	val tokens: Tokens? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class Access(

	@field:SerializedName("expires")
	val expires: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class Tokens(

	@field:SerializedName("access")
	val access: Access? = null,

	@field:SerializedName("refresh")
	val refresh: Refresh? = null
)

data class Refresh(

	@field:SerializedName("expires")
	val expires: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class User(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("isEmailVerified")
	val isEmailVerified: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
