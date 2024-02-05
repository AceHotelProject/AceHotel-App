package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.local.datastore.UserManager
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.data.source.remote.response.images.UploadImagesResponse
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.repository.IAuthRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.AuthDataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val userManager: UserManager,
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
        return userManager.saveAccessToken(token)
    }

    override suspend fun saveRefreshToken(token: String) {
        return userManager.saveRefreshToken(token)
    }

    override fun getAccessToken(): Flow<String> {
        return userManager.getAccessToken()
    }

    override fun getRefreshToken(): Flow<String> {
        return userManager.getRefreshToken()
    }

    override suspend fun deleteToken() {
        return userManager.deleteToken()
    }

    override fun uploadImage(image: MultipartBody.Part): Flow<Resource<List<String>>> {
        return object : NetworkBoundResource<List<String>, UploadImagesResponse>() {
            override suspend fun fetchFromApi(response: UploadImagesResponse): List<String> {
                return response.url ?: listOf()
            }

            override suspend fun createCall(): Flow<ApiResponse<UploadImagesResponse>> {
                return remoteDataSource.uploadImage(image)
            }
        }.asFlow()
    }
}