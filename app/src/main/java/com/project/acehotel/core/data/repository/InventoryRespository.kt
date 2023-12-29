package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.repository.IInventoryIRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.InventoryDataMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRespository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IInventoryIRepository {
    override fun getListInventory(token: String): Flow<Resource<List<Inventory>>> {
        return object : NetworkBoundResource<List<Inventory>, InventoryListResponse>() {
            override suspend fun fetchFromApi(response: InventoryListResponse): List<Inventory> {
                return InventoryDataMapper.mapInventoryListResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<InventoryListResponse>> {
                return remoteDataSource.getListInventory(token)
            }

        }.asFlow()
    }

    override fun getDetailInventory(token: String, id: String): Flow<Resource<Inventory>> {
        return object : NetworkBoundResource<Inventory, InventoryDetailResponse>() {
            override suspend fun fetchFromApi(response: InventoryDetailResponse): Inventory {
                return InventoryDataMapper.mapInventoryDetailResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<InventoryDetailResponse>> {
                return remoteDataSource.getDetailInventory(token, id)
            }
        }.asFlow()
    }
}