package com.project.acehotel.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.acehotel.core.utils.constants.UserRole
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "userId")
    var userId: String? = "",

    @ColumnInfo(name = "role")
    var role: UserRole? = UserRole.UNDEFINED,

    @ColumnInfo(name = "username")
    var username: String? = "",

    @ColumnInfo(name = "email")
    var email: String? = "",

    @Embedded
    var tokenInfo: TokenEntity
) : Parcelable