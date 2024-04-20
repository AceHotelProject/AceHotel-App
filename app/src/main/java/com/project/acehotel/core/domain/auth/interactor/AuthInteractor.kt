package com.project.acehotel.core.domain.auth.interactor

import com.project.acehotel.core.data.repository.AuthRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: AuthRepository) : AuthUseCase {
    override fun loginUser(email: String, password: String): Flow<Resource<Auth>> =
        authRepository.loginUser(email, password)

    override fun forgetPassword(email: String): Flow<Resource<Int>> =
        authRepository.forgetPassword(email)

    override suspend fun insertCacheUser(user: Auth) = authRepository.insertCacheUser(user)

    override fun getUser(): Flow<Auth> = authRepository.getUser()

    override suspend fun updateUser(user: Auth) = authRepository.updateUser(user)
    override fun updateUser(id: String, hotelId: String, email: String): Flow<Resource<User>> =
        authRepository.updateUser(id, hotelId, email)


    override suspend fun deleteUser(user: Auth) = authRepository.deleteUser(user)
    override fun deleteUserAccount(id: String, hotelId: String): Flow<Resource<Int>> =
        authRepository.deleteUserAccount(id, hotelId)

    override fun saveAccessToken(token: String): Flow<Boolean> =
        authRepository.saveAccessToken(token)

    override fun saveRefreshToken(token: String): Flow<Boolean> =
        authRepository.saveRefreshToken(token)

    override fun getAccessToken(): Flow<String> = authRepository.getAccessToken()

    override fun getRefreshToken(): Flow<String> = authRepository.getRefreshToken()

    override suspend fun deleteToken() = authRepository.deleteToken()
    override fun uploadImage(image: List<MultipartBody.Part>): Flow<Resource<List<String>>> =
        authRepository.uploadImage(image)

    override fun getUserByHotel(hotelId: String): Flow<Resource<List<User>>> =
        authRepository.getUserByHotel(hotelId)

    override fun getUserById(id: String, hotelId: String): Flow<Resource<User>> =
        authRepository.getUserById(id, hotelId)

}