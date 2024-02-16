package com.project.acehotel.core.data.source.remote.response.hotel

import com.google.gson.annotations.SerializedName

data class HotelResponse(

    @field:SerializedName("room_id")
    val roomId: List<RoomIdItem?>? = null,

    @field:SerializedName("regular_room_price")
    val regularRoomPrice: Int? = null,

    @field:SerializedName("cleaning_staff_id")
    val cleaningStaffId: CleaningStaffId? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("extra_bed_price")
    val extraBedPrice: Int? = null,

    @field:SerializedName("inventory_id")
    val inventoryId: List<Any?>? = null,

    @field:SerializedName("owner_id")
    val ownerId: OwnerId? = null,

    @field:SerializedName("exclusive_room_count")
    val exclusiveRoomCount: Int? = null,

    @field:SerializedName("inventory_staff_id")
    val inventoryStaffId: InventoryStaffId? = null,

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
    val receptionistId: ReceptionistId? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("regular_room_count")
    val regularRoomCount: Int? = null
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

