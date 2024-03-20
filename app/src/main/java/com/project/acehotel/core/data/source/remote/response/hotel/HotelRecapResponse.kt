package com.project.acehotel.core.data.source.remote.response.hotel

import com.google.gson.annotations.SerializedName

data class HotelRecapResponse(

	@field:SerializedName("revenue")
	val revenue: Int? = null,

	@field:SerializedName("checkin_count")
	val checkinCount: Int? = null,

	@field:SerializedName("branch_count")
	val branchCount: Int? = null,

	@field:SerializedName("total_booking")
	val totalBooking: Int? = null
)
