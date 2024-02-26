package com.project.acehotel.core.domain.hotel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManageHotel(
    val id: String = "",

    val name: String = "",
    val address: String = "",
    val contact: String = "",

    val regularRoomCount: Int = 0,
    // for now only 1 images
    val regularRoomImage: String = "",
    val exclusiveRoomCount: Int = 0,
    // for now only 1 images
    val exclusiveRoomImage: String = "",
    val regularRoomPrice: Int = 0,
    val exclusiveRoomPrice: Int = 0,
    val extraBedPrice: Int = 0,

    val roomId: List<String> = listOf(),
    val inventoryId: List<String> = listOf(),

    val ownerId: String = "",
    val ownerName: String = "",
    val ownerEmail: String = "",

    val receptionistId: String = "",
    val receptionistName: String = "",
    val receptionistEmail: String = "",

    val cleaningStaffId: String = "",
    val cleaningStaffName: String = "",
    val cleaningStaffEmail: String = "",

    val inventoryStaffId: String = "",
    val inventoryStaffName: String = "",
    val inventoryStaffEmail: String = "",

    ) : Parcelable