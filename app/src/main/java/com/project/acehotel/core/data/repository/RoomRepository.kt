package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.room.CheckoutBody
import com.project.acehotel.core.data.source.remote.response.room.CheckoutFacility
import com.project.acehotel.core.data.source.remote.response.room.ListRoomResponse
import com.project.acehotel.core.data.source.remote.response.room.RoomResponse
import com.project.acehotel.core.domain.room.model.Room
import com.project.acehotel.core.domain.room.repository.IRoomRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.RoomDataMapper
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IRoomRepository {
    override fun getListRoomByHotel(hotelId: String): Flow<Resource<List<Room>>> {
        return object : NetworkBoundResource<List<Room>, ListRoomResponse>() {
            override suspend fun fetchFromApi(response: ListRoomResponse): List<Room> {
                return RoomDataMapper.mapListRoomResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ListRoomResponse>> {
                return remoteDataSource.getListRoomByHotel(hotelId)
            }

        }.asFlow()
    }

    override fun getRoomDetail(roomId: String): Flow<Resource<Room>> {
        return object : NetworkBoundResource<Room, RoomResponse>() {
            override suspend fun fetchFromApi(response: RoomResponse): Room {
                return RoomDataMapper.mapRoomResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<RoomResponse>> {
                return remoteDataSource.getRoomDetail(roomId)
            }

        }.asFlow()
    }

    override fun roomCheckin(
        roomId: String,
        checkinDate: String,
        bookingId: String,
        visitorId: String
    ): Flow<Resource<Room>> {
        return object : NetworkBoundResource<Room, RoomResponse>() {
            override suspend fun fetchFromApi(response: RoomResponse): Room {
                return RoomDataMapper.mapRoomResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<RoomResponse>> {
                return remoteDataSource.roomCheckin(roomId, checkinDate, bookingId, visitorId)
            }

        }.asFlow()
    }

    override fun roomCheckout(
        roomId: String,
        checkoutDate: String,
        bookingId: String,
        visitorId: String,
        facilityBantalPutih: Boolean,
        facilityBantalHitam: Boolean,
        facilityTv: Boolean,
        facilityRemoteTv: Boolean,
        facilityRemoteAc: Boolean,
        facilityGantunganVaju: Boolean,
        facilityKarpet: Boolean,
        facilityCerminWastafel: Boolean,
        facilityShower: Boolean,
        facilitySelendang: Boolean,
        facilityKerangjangSampah: Boolean,
        facilityKursi: Boolean,
        note: String,
    ): Flow<Resource<Room>> {
        return object : NetworkBoundResource<Room, RoomResponse>() {
            override suspend fun fetchFromApi(response: RoomResponse): Room {
                return RoomDataMapper.mapRoomResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<RoomResponse>> {
                val checkoutFacility = CheckoutFacility(
                    facilityBantalPutih,
                    facilityBantalHitam,
                    facilityTv,
                    facilityRemoteTv,
                    facilityRemoteAc,
                    facilityGantunganVaju,
                    facilityKarpet,
                    facilityCerminWastafel,
                    facilityShower,
                    facilitySelendang,
                    facilityKerangjangSampah,
                    facilityKursi
                )

                val checkoutBody = CheckoutBody(
                    checkoutDate, bookingId, visitorId, checkoutFacility, note
                )

                return remoteDataSource.roomCheckout(
                    roomId, checkoutBody
                )
            }

        }.asFlow()
    }

    override fun deleteRoom(id: String): Flow<Resource<Int>> {
        return object : NetworkBoundResource<Int, Response<RoomResponse>>() {
            override suspend fun fetchFromApi(response: Response<RoomResponse>): Int {
                return response.code()
            }

            override suspend fun createCall(): Flow<ApiResponse<Response<RoomResponse>>> {
                return remoteDataSource.deleteRoom(id)
            }
        }.asFlow()
    }
}