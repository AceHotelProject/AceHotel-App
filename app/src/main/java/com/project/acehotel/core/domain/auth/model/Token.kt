package com.project.acehotel.core.domain.auth.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tokens(
    val accessToken: TokensFormat,
    val refreshToken: TokensFormat,
): Parcelable

@Parcelize
data class TokensFormat(
    val token: String,
    val expires: String,
): Parcelable