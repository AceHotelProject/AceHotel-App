package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.local.datastore.TokenManager
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.repository.IAuthRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.AuthDataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val tokenManager: TokenManager,
) : IAuthRepository {

    override fun loginUser(email: String, password: String): Flow<Resource<Auth>> {
        return object : NetworkBoundResource<Auth, AuthResponse>() {
            override suspend fun fetchFromApi(response: AuthResponse): Auth {
                return AuthDataMapper.mapAuthResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<AuthResponse>> {
                return remoteDataSource.loginUser(email, password)
            }
        }.asFlow()
    }

    override suspend fun insertCacheUser(user: Auth) {
        val userEntity = AuthDataMapper.mapAuthToEntity(user)

        return appExecutors.diskIO().execute {
            GlobalScope.launch(Dispatchers.IO) {
                localDataSource.insertUser(userEntity)
            }
        }
    }

    override fun getUser(): Flow<Auth> {
        return localDataSource.getUser().map {
            AuthDataMapper.mapUserEntityToDomain(it)
        }
    }

    override suspend fun updateUser(user: Auth) {
        val userEntity = AuthDataMapper.mapAuthToEntity(user)

        return appExecutors.diskIO().execute {
            GlobalScope.launch(Dispatchers.IO) {
                localDataSource.updateUser(userEntity)
            }
        }
    }

    override suspend fun deleteUser(user: Auth) {
        val userEntity = AuthDataMapper.mapAuthToEntity(user)

        return appExecutors.diskIO().execute {
            GlobalScope.launch(Dispatchers.IO) {
                localDataSource.deleteUser(userEntity)
            }
        }
    }

    override suspend fun saveAccessToken(token: String) {
        return tokenManager.saveAccessToken(token)
    }

    override suspend fun saveRefreshToken(token: String) {
        return tokenManager.saveRefreshToken(token)
    }

    override fun getAccessToken(): Flow<String> {
        return tokenManager.getAccessToken()
    }

    override fun getRefreshToken(): Flow<String> {
        return tokenManager.getRefreshToken()
    }

    override suspend fun deleteToken() {
        return tokenManager.deleteToken()
    }
}