package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.local.datastore.UserManager
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.hotel.CreateHotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ManageHotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ManageHotelResultItem
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.domain.hotel.repository.IHotelRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.HotelDataMapper
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val userManager: UserManager
) : IHotelRepository {
    override fun addHotel(
        name: String,
        address: String,
        contact: String,
        regularRoomCount: Int,
        regularRoomImage: String,
        exclusiveRoomCount: Int,
        exclusiveRoomImage: String,
        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int,
        ownerName: String,
        ownerEmail: String,
        ownerPassword: String,
        receptionistName: String,
        receptionistEmail: String,
        receptionistPassword: String,
        cleaningName: String,
        cleaningEmail: String,
        cleaningPassword: String,
        inventoryName: String,
        inventoryEmail: String,
        inventoryPassword: String
    ): Flow<Resource<ManageHotel>> {
        return object : NetworkBoundResource<ManageHotel, CreateHotelResponse>() {
            override suspend fun fetchFromApi(response: CreateHotelResponse): ManageHotel {
                return HotelDataMapper.mapCreateHotelResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<CreateHotelResponse>> {
                return remoteDataSource.addHotel(
                    name,
                    address,
                    contact,
                    regularRoomCount,
                    regularRoomImage,
                    exclusiveRoomCount,
                    exclusiveRoomImage,
                    regularRoomPrice,
                    exclusiveRoomPrice,
                    extraBedPrice,
                    ownerName,
                    ownerEmail,
                    ownerPassword,
                    receptionistName,
                    receptionistEmail,
                    receptionistPassword,
                    cleaningName,
                    cleaningEmail,
                    cleaningPassword,
                    inventoryName,
                    inventoryEmail,
                    inventoryPassword
                )
            }

        }.asFlow()
    }

    override fun getListHotel(): Flow<Resource<List<ManageHotel>>> {
        return object : NetworkBoundResource<List<ManageHotel>, ManageHotelResponse>() {
            override suspend fun fetchFromApi(response: ManageHotelResponse): List<ManageHotel> {
                return HotelDataMapper.mapListManageHotelResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ManageHotelResponse>> {
                return remoteDataSource.getListHotel()
            }
        }.asFlow()
    }

    override fun updateHotel(
        id: String,
        name: String,
        address: String,
        contact: String,
        regularRoomCount: Int,
        regularRoomImage: String,
        exclusiveRoomCount: Int,
        exclusiveRoomImage: String,
        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int,
        ownerName: String,
        ownerEmail: String,
        ownerPassword: String,
        receptionistName: String,
        receptionistEmail: String,
        receptionistPassword: String,
        cleaningName: String,
        cleaningEmail: String,
        cleaningPassword: String,
        inventoryName: String,
        inventoryEmail: String,
        inventoryPassword: String
    ): Flow<Resource<ManageHotel>> {
        return object : NetworkBoundResource<ManageHotel, ManageHotelResultItem>() {
            override suspend fun fetchFromApi(response: ManageHotelResultItem): ManageHotel {
                return HotelDataMapper.mapManageHotelResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ManageHotelResultItem>> {
                return remoteDataSource.updateHotel(
                    id,
                    name,
                    address,
                    contact,
                    regularRoomCount,
                    regularRoomImage,
                    exclusiveRoomCount,
                    exclusiveRoomImage,
                    regularRoomPrice,
                    exclusiveRoomPrice,
                    extraBedPrice,
                    ownerName,
                    ownerEmail,
                    ownerPassword,
                    receptionistName,
                    receptionistEmail,
                    receptionistPassword,
                    cleaningName,
                    cleaningEmail,
                    cleaningPassword,
                    inventoryName,
                    inventoryEmail,
                    inventoryPassword
                )
            }
        }.asFlow()
    }

    override fun getSelectedHotel(): Flow<String> {
        return userManager.getCurrentHotelId()
    }

    override suspend fun saveSelectedHotel(id: String) {
        return userManager.saveCurrentHotelId(id)
    }

    override fun deleteHotel(id: String): Flow<Resource<Int>> {
        return object : NetworkBoundResource<Int, Response<ManageHotelResultItem>>() {
            override suspend fun fetchFromApi(response: Response<ManageHotelResultItem>): Int {
                return response.code()
            }

            override suspend fun createCall(): Flow<ApiResponse<Response<ManageHotelResultItem>>> {
                return remoteDataSource.deleteHotel(id)
            }

        }.asFlow()
    }


}