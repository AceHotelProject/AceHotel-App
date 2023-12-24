package com.project.acehotel.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.acehotel.core.domain.auth.model.TokensFormat
import com.project.acehotel.core.utils.constants.UserRole
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userId")
    var id: String,

    @ColumnInfo(name = "role")
    var role: UserRole,

    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "accessToken")
    var accessToken: TokensFormat,

    @ColumnInfo(name = "refreshToken")
    var refreshToken: TokensFormat,
) : Parcelable