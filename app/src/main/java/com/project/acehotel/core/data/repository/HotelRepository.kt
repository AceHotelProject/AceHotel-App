package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.local.datastore.UserManager
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.hotel.HotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ListHotelResponse
import com.project.acehotel.core.domain.hotel.model.Hotel
import com.project.acehotel.core.domain.hotel.repository.IHotelRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.HotelDataMapper
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
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
        regularRoomImage: MultipartBody.Part,
        exclusiveRoomCount: Int,
        exclusiveRoomImage: MultipartBody.Part,
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
    ): Flow<Resource<Hotel>> {
        return object : NetworkBoundResource<Hotel, HotelResponse>() {
            override suspend fun fetchFromApi(response: HotelResponse): Hotel {
                return HotelDataMapper.mapHotelResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<HotelResponse>> {
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

    override fun getHotels(): Flow<Resource<List<Hotel>>> {
        return object : NetworkBoundResource<List<Hotel>, ListHotelResponse>() {
            override suspend fun fetchFromApi(response: ListHotelResponse): List<Hotel> {
                return HotelDataMapper.mapListHotelResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ListHotelResponse>> {
                return remoteDataSource.getHotels()
            }
        }.asFlow()
    }

    override fun updateHotel(
        id: String,
        name: String,
        address: String,
        contact: String,
        regularRoomCount: Int,
        regularRoomImage: MultipartBody.Part,
        exclusiveRoomCount: Int,
        exclusiveRoomImage: MultipartBody.Part,
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
    ): Flow<Resource<Hotel>> {
        return object : NetworkBoundResource<Hotel, HotelResponse>() {
            override suspend fun fetchFromApi(response: HotelResponse): Hotel {
                return HotelDataMapper.mapHotelResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<HotelResponse>> {
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


}