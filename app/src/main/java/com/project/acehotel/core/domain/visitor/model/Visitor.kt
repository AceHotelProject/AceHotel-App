package com.project.acehotel.core.domain.visitor.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Visitor(
    val id: String,
    val name: String,
    val address: String,
    val identity_num: String,
    val phone: String,
    val email: String,
    val identityImage: String,
) : Parcelable