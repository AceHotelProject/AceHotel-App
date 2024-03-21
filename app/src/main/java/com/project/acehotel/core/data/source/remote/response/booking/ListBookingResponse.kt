package com.project.acehotel.core.data.source.remote.response.booking

import com.google.gson.annotations.SerializedName

data class ListBookingResponse(

    @field:SerializedName("totalResults")
    val totalResults: Int? = null,

    @field:SerializedName("limit")
    val limit: Int? = null,

    @field:SerializedName("totalPages")
    val totalPages: Int? = null,

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("results")
    val results: List<ListBookingResultsItem?>? = null
)

data class RoomItem(

    @field:SerializedName("_id")
    val noteId: String? = null,

    @field:SerializedName("id")
    val roomId: String? = null,

    @field:SerializedName("has_problem")
    val hasProblem: Boolean? = null,

    @field:SerializedName("checkout_staff_id")
    val checkoutStaffId: String? = null,

    @field:SerializedName("actual_checkin")
    val actualCheckin: String? = null,

    @field:SerializedName("checkin_staff_id")
    val checkinStaffId: String? = null,

    @field:SerializedName("actual_checkout")
    val actualCheckout: String? = null
)

data class VisitorId(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null
)

data class ListBookingResultsItem(

    @field:SerializedName("is_proof_uploaded")
    val isProofUploaded: Boolean? = null,

    @field:SerializedName("add_on_id")
    val addOnId: List<Any?>? = null,

    @field:SerializedName("total_price")
    val totalPrice: Int? = null,

    @field:SerializedName("room_count")
    val roomCount: Int? = null,

    @field:SerializedName("hotel_id")
    val hotelId: String? = null,

    @field:SerializedName("visitor_id")
    val visitorId: VisitorId? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("path_transaction_proof")
    val pathTransactionProof: String? = null,

    @field:SerializedName("checkout_date")
    val checkoutDate: String? = null,

    @field:SerializedName("room")
    val room: List<RoomItem?>? = null,

    @field:SerializedName("duration")
    val duration: Int? = null,

    @field:SerializedName("checkin_date")
    val checkinDate: String? = null,

    @field:SerializedName("id")
    val id: String? = null
)
