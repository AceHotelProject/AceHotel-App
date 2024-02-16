package com.project.acehotel.core.domain.room.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Facility(
    val bantalPutih: Boolean,
    val bantalHitam: Boolean,
    val tv: Boolean,
    val remoteTV: Boolean,
    val remoteAC: Boolean,
    val gantunganBaju: Boolean,
    val karpet: Boolean,
    val cerminWastafel: Boolean,
    val shower: Boolean,
    val selendang: Boolean,
    val keranjangSampah: Boolean,
    val kursi: Boolean,
) : Parcelable
