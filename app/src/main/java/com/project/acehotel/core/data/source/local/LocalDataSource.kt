package com.project.acehotel.core.data.source.local

import com.project.acehotel.core.data.source.local.entity.TokenData
import com.project.acehotel.core.data.source.local.entity.UserEntity
import com.project.acehotel.core.data.source.local.room.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val userDao: UserDao) {

    fun getUser(): Flow<UserEntity> = userDao.getUser()

    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    suspend fun deleteUser(user: UserEntity) {
        return userDao.deleteUser(user)
    }

    fun getTokens(): Flow<TokenData> {
        return userDao.getTokens()
    }
}