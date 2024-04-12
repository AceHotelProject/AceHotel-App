package com.project.acehotel.core.data.source.remote.response.user

import com.google.gson.annotations.SerializedName

data class ListUserResponse(

    @field:SerializedName("totalResults")
    val totalResults: Int? = null,

    @field:SerializedName("limit")
    val limit: Int? = null,

    @field:SerializedName("totalPages")
    val totalPages: Int? = null,

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("results")
    val results: List<UserResponse?>? = null
)
