package com.project.acehotel.core.domain.booking.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: String,
    val detail: String,
    val bookingId: String,
) : Parcelable
