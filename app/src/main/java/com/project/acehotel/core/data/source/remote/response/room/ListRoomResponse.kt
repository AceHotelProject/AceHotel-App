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

data class ResultsItem(

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("hotel_id")
	val hotelId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

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

data class BookingsItem(

	@field:SerializedName("booking_id")
	val bookingId: String? = null,

	@field:SerializedName("checkin_date")
	val checkinDate: String? = null,

	@field:SerializedName("visitor_id")
	val visitorId: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("checkout_date")
	val checkoutDate: String? = null
)
