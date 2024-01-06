package com.project.acehotel.core.data.source.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenData(
    val accessToken: String?,
    val accessTokenExpire: String?,
    val refreshToken: String?,
    val refreshTokenExpire: String?
) : Parcelable