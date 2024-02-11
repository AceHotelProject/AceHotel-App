package com.project.acehotel.core.domain.hotel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseHotel(
    val hotel: ListHotel,
    var isChecked: Boolean,
) : Parcelable