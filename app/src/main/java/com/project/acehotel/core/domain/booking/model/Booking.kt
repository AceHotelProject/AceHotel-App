package com.project.acehotel.core.domain.booking.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    val roomId: List<String>,
    val addOn: List<String>,
    val noteId: List<String>,
    val isProofUploaded: Boolean,
    val transactionProof: String,
    val hasProblem: Boolean,
    val hotelId: String,
    val visitorId: String,
    val visitorName: String,
    val checkinDate: String,
    val checkoutDate: String,
    val duration: Int,
    val roomCount: Int,
    val type: String,
    val totalPrice: Int,
    val id: String
) : Parcelable
