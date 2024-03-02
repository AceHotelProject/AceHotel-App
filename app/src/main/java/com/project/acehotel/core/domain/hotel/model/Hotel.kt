package com.project.acehotel.core.domain.hotel.model

import android.os.Parcelable
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.domain.room.model.Room
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

    val roomId: List<Room>,
    val inventoryId: List<String>,

    val owner: User,
    val receptionist: User,
    val inventoryStaff: User,
    val cleaningStaff: User,

    val discountAmount: Int,
    val discountCode: String,

    ) : Parcelable