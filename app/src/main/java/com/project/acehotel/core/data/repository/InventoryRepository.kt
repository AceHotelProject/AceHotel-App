package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryUpdateHistoryItem
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.model.InventoryHistory
import com.project.acehotel.core.domain.inventory.repository.IInventoryIRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.InventoryDataMapper
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IInventoryIRepository {
    override fun getListInventory(
        hotelId: String,
        name: String,
        type: String
    ): Flow<Resource<List<Inventory>>> {
        return object : NetworkBoundResource<List<Inventory>, InventoryListResponse>() {
            override suspend fun fetchFromApi(response: InventoryListResponse): List<Inventory> {
                return InventoryDataMapper.mapInventoryListResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<InventoryListResponse>> {
                return remoteDataSource.getListInventory(hotelId, name, type)
            }

        }.asFlow()
    }

    override fun getInventoryHistoryList(
        id: String,
        key: String
    ): Flow<Resource<List<InventoryHistory>>> {
        return object :
            NetworkBoundResource<List<InventoryHistory>, List<InventoryUpdateHistoryItem?>?>() {
            override suspend fun fetchFromApi(response: List<InventoryUpdateHistoryItem?>?): List<InventoryHistory> {
                return InventoryDataMapper.mapListInventoryHistoryToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<InventoryUpdateHistoryItem?>?>> {
                return remoteDataSource.getInventoryHistoryList(id, key)
            }
        }.asFlow()
    }

    override fun getDetailInventory(id: String, hotelId: String): Flow<Resource<Inventory>> {
        return object : NetworkBoundResource<Inventory, InventoryDetailResponse>() {
            override suspend fun fetchFromApi(response: InventoryDetailResponse): Inventory {
                return InventoryDataMapper.mapInventoryDetailResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<InventoryDetailResponse>> {
                return remoteDataSource.getDetailInventory(id, hotelId)
            }
        }.asFlow()
    }

    override fun addInventory(
        hotelId: String,
        name: String,
        type: String,
        stock: Int
    ): Flow<Resource<Inventory>> {
        return object : NetworkBoundResource<Inventory, InventoryDetailResponse>() {
            override suspend fun fetchFromApi(response: InventoryDetailResponse): Inventory {
                return InventoryDataMapper.mapInventoryDetailResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<InventoryDetailResponse>> {
                return remoteDataSource.addInventory(hotelId, name, type, stock)
            }
        }.asFlow()
    }

    override fun updateInventory(
        id: String,
        hotelId: String,
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ): Flow<Resource<Inventory>> {
        return object : NetworkBoundResource<Inventory, InventoryDetailResponse>() {
            override suspend fun fetchFromApi(response: InventoryDetailResponse): Inventory {
                return InventoryDataMapper.mapInventoryDetailResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<InventoryDetailResponse>> {
                return remoteDataSource.updateInventory(
                    id,
                    hotelId,
                    name,
                    type,
                    stock,
                    title,
                    description
                )
            }
        }.asFlow()
    }

    override fun deleteInventory(id: String, hotelId: String): Flow<Resource<Int>> {
        return object : NetworkBoundResource<Int, Response<InventoryDetailResponse>>() {
            override suspend fun fetchFromApi(response: Response<InventoryDetailResponse>): Int {
                return response.code()
            }

            override suspend fun createCall(): Flow<ApiResponse<Response<InventoryDetailResponse>>> {
                return remoteDataSource.deleteInventory(id, hotelId)
            }
        }.asFlow()
    }
}