package com.project.acehotel.core.data.source.remote.response.room

import com.google.gson.annotations.SerializedName

data class ListRoomResponse(

    @field:SerializedName("totalResults")
    val totalResults: Int? = null,

    @field:SerializedName("limit")
    val limit: Int? = null,

    @field:SerializedName("totalPages")
    val totalPages: Int? = null,

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("results")
    val results: List<ResultsItem?>? = null
)

data class ResultsItem(

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("hotel_id")
    val hotelId: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("bookings")
    val bookings: List<BookingsItem?>? = null,

    @field:SerializedName("facility")
    val facility: Facility? = null,

    @field:SerializedName("is_booked")
    val isBooked: Boolean? = null,

    @field:SerializedName("is_clean")
    val isClean: Boolean? = null
)
