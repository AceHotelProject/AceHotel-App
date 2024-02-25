package com.project.acehotel.core.data.source.remote.response.booking

import com.google.gson.annotations.SerializedName

data class AddBookingResponse(

	@field:SerializedName("room_id")
	val roomId: List<RoomIdItem?>? = null,

	@field:SerializedName("note_id")
	val noteId: List<Any?>? = null,

	@field:SerializedName("is_proof_uploaded")
	val isProofUploaded: Boolean? = null,

	@field:SerializedName("add_on_id")
	val addOnId: List<String?>? = null,

	@field:SerializedName("total_price")
	val totalPrice: Int? = null,

	@field:SerializedName("room_count")
	val roomCount: Int? = null,

	@field:SerializedName("hotel_id")
	val hotelId: String? = null,

	@field:SerializedName("visitor_id")
	val visitorId: String? = null,

	@field:SerializedName("has_problem")
	val hasProblem: Boolean? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("checkout_date")
	val checkoutDate: String? = null,

	@field:SerializedName("duration")
	val duration: Int? = null,

	@field:SerializedName("checkin_date")
	val checkinDate: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class RoomIdItem(

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("hotel_id")
	val hotelId: String? = null,

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

data class Facility(

	@field:SerializedName("karpet")
	val karpet: Boolean? = null,

	@field:SerializedName("shower")
	val shower: Boolean? = null,

	@field:SerializedName("tv")
	val tv: Boolean? = null,

	@field:SerializedName("bantal_hitam")
	val bantalHitam: Boolean? = null,

	@field:SerializedName("kursi")
	val kursi: Boolean? = null,

	@field:SerializedName("bantal_putih")
	val bantalPutih: Boolean? = null,

	@field:SerializedName("selendang")
	val selendang: Boolean? = null,

	@field:SerializedName("remote_tv")
	val remoteTv: Boolean? = null,

	@field:SerializedName("remote_ac")
	val remoteAc: Boolean? = null,

	@field:SerializedName("cermin_wastafel")
	val cerminWastafel: Boolean? = null,

	@field:SerializedName("kerangjang_sampah")
	val kerangjangSampah: Boolean? = null,

	@field:SerializedName("gantungan_baju")
	val gantunganBaju: Boolean? = null
)
