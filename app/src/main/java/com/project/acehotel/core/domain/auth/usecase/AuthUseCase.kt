package com.project.acehotel.core.domain.auth.usecase

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.Auth
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {

    fun loginUser(email: String, password: String): Flow<Resource<Auth>>

    suspend fun insertCacheUser(user: Auth)

    fun getUser(): Flow<Auth>

    suspend fun updateUser(user: Auth)

    suspend fun deleteUser(user: Auth)

    suspend fun saveAccessToken(token: String)

    suspend fun saveRefreshToken(token: String)

    fun getAccessToken(): Flow<String>

    fun getRefreshToken(): Flow<String>

    suspend fun deleteToken()
}