package com.project.acehotel.core.data.source.remote.response.room

data class CheckoutBody(
    val checkout_date: String,
    val booking_id: String,
    val visitor_id: String,
    val facility: CheckoutFacility,
    val note: String,
)

data class CheckoutFacility(
    val bantal_putih: Boolean,
    val bantal_hitam: Boolean,
    val tv: Boolean,
    val remote_tv: Boolean,
    val remote_ac: Boolean,
    val gantungan_baju: Boolean,
    val karpet: Boolean,
    val cermin_wastafel: Boolean,
    val shower: Boolean,
    val selendang: Boolean,
    val kerangjang_sampah: Boolean,
    val kursi: Boolean
)
