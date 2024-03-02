package com.project.acehotel.core.data.source.remote.response.room

import com.google.gson.annotations.SerializedName

data class RoomResponse(

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("hotel_id")
    val hotelId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("bookings")
    val bookings: List<Any?>? = null,

    @field:SerializedName("facility")
    val facility: Facility? = null,

    @field:SerializedName("is_booked")
    val isBooked: Boolean? = null,

    @field:SerializedName("is_clean")
    val isClean: Boolean? = null
)
