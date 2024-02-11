package com.project.acehotel.core.domain.hotel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManageHotel(
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

    val ownerName: String,
    val ownerEmail: String,

    val receptionistName: String,
    val receptionistEmail: String,

    val cleaningStaffName: String,
    val cleaningStaffEmail: String,

    val inventoryStaffName: String,
    val inventoryStaffEmail: String,

    ) : Parcelable