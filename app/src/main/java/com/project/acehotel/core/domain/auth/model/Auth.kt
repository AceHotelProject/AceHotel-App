package com.project.acehotel.core.domain.auth.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Auth(
    val id: Int,
    val user: User?,
    val tokens: Tokens?,
) : Parcelable