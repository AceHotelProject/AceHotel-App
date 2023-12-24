package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.repository.IAuthRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.AuthDataMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): IAuthRepository{

    override fun loginUser(email: String, password: String): Flow<Resource<Auth>> {
        return object : NetworkBoundResource<Auth, AuthResponse>(){
            override suspend fun fetchFromApi(response: AuthResponse): Auth {
                return AuthDataMapper.mapAuthResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<AuthResponse>> {
                return remoteDataSource.loginUser(email, password)
            }
        }.asFlow()
    }
}