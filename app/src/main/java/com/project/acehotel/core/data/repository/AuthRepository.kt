package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.local.datastore.DatastoreManager
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.data.source.remote.response.images.UploadImagesResponse
import com.project.acehotel.core.data.source.remote.response.user.ListUserResponse
import com.project.acehotel.core.data.source.remote.response.user.UserResponse
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.domain.auth.repository.IAuthRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.AuthDataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val datastoreManager: DatastoreManager,
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

    override fun forgetPassword(email: String): Flow<Resource<Int>> {
        return object : NetworkBoundResource<Int, Response<AuthResponse>>() {
            override suspend fun fetchFromApi(response: Response<AuthResponse>): Int {
                return response.code()
            }

            override suspend fun createCall(): Flow<ApiResponse<Response<AuthResponse>>> {
                return remoteDataSource.forgetPassword(email)
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

    override fun updateUser(
        id: String,
        hotelId: String,
        email: String,
    ): Flow<Resource<User>> {
        return object : NetworkBoundResource<User, UserResponse>() {
            override suspend fun fetchFromApi(response: UserResponse): User {
                return AuthDataMapper.mapUserResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<UserResponse>> {
                return remoteDataSource.updateUser(id, hotelId, email)
            }
        }.asFlow()
    }

    override suspend fun deleteUser(user: Auth) {
        val userEntity = AuthDataMapper.mapAuthToEntity(user)

        return appExecutors.diskIO().execute {
            GlobalScope.launch(Dispatchers.IO) {
                localDataSource.deleteUser(userEntity)
            }
        }
    }

    override fun deleteUserAccount(id: String, hotelId: String): Flow<Resource<Int>> {
        return object : NetworkBoundResource<Int, Response<UserResponse>>() {
            override suspend fun fetchFromApi(response: Response<UserResponse>): Int {
                return response.code()
            }

            override suspend fun createCall(): Flow<ApiResponse<Response<UserResponse>>> {
                return remoteDataSource.deleteUser(id, hotelId)
            }

        }.asFlow()
    }

    override fun saveAccessToken(token: String): Flow<Boolean> {
        return datastoreManager.saveAccessToken(token)
    }

    override fun saveRefreshToken(token: String): Flow<Boolean> {
        return datastoreManager.saveRefreshToken(token)
    }

    override fun getAccessToken(): Flow<String> {
        return datastoreManager.getAccessToken()
    }

    override fun getRefreshToken(): Flow<String> {
        return datastoreManager.getRefreshToken()
    }

    override suspend fun deleteToken() {
        return datastoreManager.deleteToken()
    }

    override fun uploadImage(image: List<MultipartBody.Part>): Flow<Resource<List<String>>> {
        return object : NetworkBoundResource<List<String>, UploadImagesResponse>() {
            override suspend fun fetchFromApi(response: UploadImagesResponse): List<String> {
                return (response.path ?: listOf()) as List<String>
            }

            override suspend fun createCall(): Flow<ApiResponse<UploadImagesResponse>> {
                return remoteDataSource.uploadImage(image)
            }
        }.asFlow()
    }

    override fun getUserByHotel(hotelId: String): Flow<Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, ListUserResponse>() {
            override suspend fun fetchFromApi(response: ListUserResponse): List<User> {
                return AuthDataMapper.mapListUserResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ListUserResponse>> {
                return remoteDataSource.getUserByHotel(hotelId)
            }

        }.asFlow()
    }

    override fun getUserById(id: String, hotelId: String): Flow<Resource<User>> {
        return object : NetworkBoundResource<User, UserResponse>() {
            override suspend fun fetchFromApi(response: UserResponse): User {
                return AuthDataMapper.mapUserResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<UserResponse>> {
                return remoteDataSource.getUserById(id, hotelId)
            }
        }.asFlow()
    }
}