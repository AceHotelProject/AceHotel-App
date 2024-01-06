package com.project.acehotel.core.data.source.local.room

import androidx.room.*
import com.project.acehotel.core.data.source.local.entity.TokenData
import com.project.acehotel.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user ORDER BY userId DESC LIMIT 1")
    fun getUser(): Flow<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT accessToken, accessTokenExpire, refreshToken, refreshTokenExpire FROM user ORDER BY userId DESC LIMIT 1")
    fun getTokens(): Flow<TokenData>

    @Delete
    suspend fun deleteUser(user: UserEntity)

}