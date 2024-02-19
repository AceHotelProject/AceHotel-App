package com.project.acehotel.core.data.source.remote.response.visitor

import com.google.gson.annotations.SerializedName

data class VisitorResponse(

    @field:SerializedName("path_identity_image")
    val pathIdentityImage: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("identity_num")
    val identityNum: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("hotel_id")
    val hotelId: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)
