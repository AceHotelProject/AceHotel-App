package com.project.acehotel.core.domain.auth.model

import android.os.Parcelable
import com.project.acehotel.core.utils.constants.UserRole
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val role: UserRole?,
    // isEmailVerified is not used
    val username: String?,
    val email: String?,
    val id: String?,
    val hotelId: List<String?>?
) : Parcelable