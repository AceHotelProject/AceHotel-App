package com.project.acehotel.core.domain.auth.interactor

import com.project.acehotel.core.data.repository.AuthRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: AuthRepository) : AuthUseCase {
    override fun loginUser(email: String, password: String): Flow<Resource<Auth>> =
        authRepository.loginUser(email, password)

    override suspend fun insertCacheUser(user: Auth) = authRepository.insertCacheUser(user)

    override fun getUser(): Flow<Auth> = authRepository.getUser()

    override fun updateUser(user: Auth) = authRepository.updateUser(user)

    override fun deleteUser(user: Auth) = authRepository.deleteUser(user)
}