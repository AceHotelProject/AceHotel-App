package com.project.acehotel.core.domain.hotel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hotel(
    val id: String,

    val name: String,
    val address: String,
    val contact: String,

    val regularRoomCount: Int,
    // for now only 1 images
    val regularRoomImage: String,
    val exclusiveRoomCount: Int,
    // for now only 1 images
    val exclusiveRoomImage: String,
    val regularRoomPrice: Int,
    val exclusiveRoomPrice: Int,
    val extraBedPrice: Int,

    val roomId: List<String>,
    val inventoryId: List<String>,

    val ownerId: String,
    val receptionistId: String,
    val cleaningStaffId: String,
    val inventoryStaffId: String,
) : Parcelable