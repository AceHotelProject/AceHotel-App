package com.project.acehotel.core.data.source.remote

import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.network.ApiService
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.data.source.remote.response.auth.RefreshTokenResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    // AUTH

    suspend fun loginUser(email: String, password: String): Flow<ApiResponse<AuthResponse>> {
        return flow {
            try {
                val response = apiService.loginUser(email, password)

                if (!response.user?.id.isNullOrEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.tag("RemoteDataSource").e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun refreshToken(refreshToken: String): Flow<ApiResponse<RefreshTokenResponse>> {
        return flow {
            try {
                val response = apiService.refreshToken(refreshToken)

                if (!response.access?.token.isNullOrEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: java.lang.Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.tag("RemoteDataSource").e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
    // AUTH

    // INVENTORY

    suspend fun getListInventory(): Flow<ApiResponse<InventoryListResponse>> {
        return flow<ApiResponse<InventoryListResponse>> {
            try {
                val response = apiService.getListInventory()

                if (!response.results.isNullOrEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.tag("RemoteDataSource").e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailInventory(
        token: String,
        id: String
    ): Flow<ApiResponse<InventoryDetailResponse>> {
        return flow<ApiResponse<InventoryDetailResponse>> {
            try {
                val response = apiService.getDetailInventory(token, id)

                if (response.name != null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.tag("RemoteDataSource").e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    // INVENTORY
}