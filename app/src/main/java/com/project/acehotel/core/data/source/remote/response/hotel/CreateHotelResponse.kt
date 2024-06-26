package com.project.acehotel.core.data.source.remote.response.hotel

import com.google.gson.annotations.SerializedName

data class CreateHotelResponse(

	@field:SerializedName("room_id")
	val roomId: List<String?>? = null,

	@field:SerializedName("regular_room_price")
	val regularRoomPrice: Int? = null,

	@field:SerializedName("cleaning_staff_id")
	val cleaningStaffId: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("extra_bed_price")
	val extraBedPrice: Int? = null,

	@field:SerializedName("inventory_id")
	val inventoryId: List<Any?>? = null,

	@field:SerializedName("owner_id")
	val ownerId: String? = null,

	@field:SerializedName("exclusive_room_count")
	val exclusiveRoomCount: Int? = null,

	@field:SerializedName("inventory_staff_id")
	val inventoryStaffId: String? = null,

	@field:SerializedName("revenue")
	val revenue: Int? = null,

	@field:SerializedName("regular_room_image_path")
	val regularRoomImagePath: List<String?>? = null,

	@field:SerializedName("contact")
	val contact: String? = null,

	@field:SerializedName("exclusive_room_image_path")
	val exclusiveRoomImagePath: List<String?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("exclusive_room_price")
	val exclusiveRoomPrice: Int? = null,

	@field:SerializedName("receptionist_id")
	val receptionistId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("regular_room_count")
	val regularRoomCount: Int? = null
)
