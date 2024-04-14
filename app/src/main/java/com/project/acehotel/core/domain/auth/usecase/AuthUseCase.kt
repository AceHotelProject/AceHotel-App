package com.project.acehotel.core.domain.auth.usecase

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.model.User
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface AuthUseCase {

    fun loginUser(email: String, password: String): Flow<Resource<Auth>>

    fun forgetPassword(email: String): Flow<Resource<Int>>

    suspend fun insertCacheUser(user: Auth)

    fun getUser(): Flow<Auth>

    suspend fun updateUser(user: Auth)

    suspend fun deleteUser(user: Auth)

    suspend fun saveAccessToken(token: String)

    suspend fun saveRefreshToken(token: String)

    fun getAccessToken(): Flow<String>

    fun getRefreshToken(): Flow<String>

    suspend fun deleteToken()

    fun uploadImage(image: List<MultipartBody.Part>): Flow<Resource<List<String>>>

    fun getUserByHotel(hotelId: String): Flow<Resource<List<User>>>

    fun getUserById(id: String, hotelId: String): Flow<Resource<User>>

    fun updateUser(
        id: String,
        hotelId: String,
        email: String,
        username: String,
        role: String,
    ): Flow<Resource<User>>

    fun deleteUserAccount(id: String, hotelId: String): Flow<Resource<Int>>
}