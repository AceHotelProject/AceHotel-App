package com.project.acehotel.core.data.source.remote.response.booking

import com.google.gson.annotations.SerializedName

data class AddBookingResponse(

    @field:SerializedName("duration")
    val duration: Int? = null,

    @field:SerializedName("is_proof_uploaded")
    val isProofUploaded: Boolean? = null,

    @field:SerializedName("add_on_id")
    val addOnId: List<Any?>? = null,

    @field:SerializedName("total_price")
    val totalPrice: Int? = null,

    @field:SerializedName("checkin_date")
    val checkinDate: String? = null,

    @field:SerializedName("room_count")
    val roomCount: Int? = null,

    @field:SerializedName("hotel_id")
    val hotelId: String? = null,

    @field:SerializedName("visitor_id")
    val visitorId: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("checkout_date")
    val checkoutDate: String? = null,

    @field:SerializedName("room")
    val room: List<AddBookingRoomItem?>? = null
)

data class AddBookingRoomItem(

    @field:SerializedName("_id")
    val noteId: String? = null,

    @field:SerializedName("id")
    val roomId: String? = null,

    @field:SerializedName("has_problem")
    val hasProblem: Boolean? = null
)
