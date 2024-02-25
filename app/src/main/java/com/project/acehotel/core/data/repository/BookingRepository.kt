package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.booking.AddBookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.BookingResponse
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.repository.IBookingRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.BookingDataMapper
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IBookingRepository {

    override fun addBooking(
        hotelId: String,
        visitorId: String,
        checkinDate: String,
        duration: Int,
        roomCount: Int,
        extraBed: Int,
        type: String
    ): Flow<Resource<Booking>> {
        return object : NetworkBoundResource<Booking, AddBookingResponse>() {
            override suspend fun fetchFromApi(response: AddBookingResponse): Booking {
                return BookingDataMapper.mapBookingResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<AddBookingResponse>> {
                return remoteDataSource.addBooking(
                    hotelId,
                    visitorId,
                    checkinDate,
                    duration,
                    roomCount,
                    extraBed,
                    type
                )
            }

        }.asFlow()
    }

    override fun deleteBooking(id: String): Flow<Resource<Int>> {
        return object : NetworkBoundResource<Int, Response<BookingResponse>>() {
            override suspend fun fetchFromApi(response: Response<BookingResponse>): Int {
                return response.code()
            }

            override suspend fun createCall(): Flow<ApiResponse<Response<BookingResponse>>> {
                return remoteDataSource.deleteBooking(id)
            }

        }.asFlow()
    }
}