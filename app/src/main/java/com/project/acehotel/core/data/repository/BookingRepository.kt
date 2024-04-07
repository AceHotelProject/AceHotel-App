package com.project.acehotel.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.paging.ListBookingPagingSource
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.network.ApiService
import com.project.acehotel.core.data.source.remote.response.booking.AddBookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.BookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.ListBookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.PayBookingResponse
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
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
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
                return BookingDataMapper.mapAddBookingResponseToDomain(response)
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

    override fun getListBookingByHotel(
        hotelId: String,
        filterDate: String,
        visitorName: String
    ): Flow<Resource<List<Booking>>> {
        return object : NetworkBoundResource<List<Booking>, ListBookingResponse>() {
            override suspend fun fetchFromApi(response: ListBookingResponse): List<Booking> {
                return BookingDataMapper.mapListBookingToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ListBookingResponse>> {
                return remoteDataSource.getListBookingByHotel(hotelId, filterDate, visitorName)
            }
        }.asFlow()
    }

    override fun getListBookingByRoom(
        roomId: String,
        filterDate: String
    ): Flow<Resource<List<Booking>>> {
        return object : NetworkBoundResource<List<Booking>, ListBookingResponse>() {
            override suspend fun fetchFromApi(response: ListBookingResponse): List<Booking> {
                return BookingDataMapper.mapListBookingToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ListBookingResponse>> {
                return remoteDataSource.getListBookingByRoom(roomId, filterDate)
            }
        }.asFlow()
    }

    override fun getListBookingByVisitor(visitorId: String): Flow<Resource<List<Booking>>> {
        return object : NetworkBoundResource<List<Booking>, ListBookingResponse>() {
            override suspend fun fetchFromApi(response: ListBookingResponse): List<Booking> {
                return BookingDataMapper.mapListBookingToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ListBookingResponse>> {
                return remoteDataSource.getListBookingByVisitor(visitorId)
            }
        }.asFlow()
    }

    override fun getDetailBooking(id: String): Flow<Resource<Booking>> {
        return object : NetworkBoundResource<Booking, BookingResponse>() {
            override suspend fun fetchFromApi(response: BookingResponse): Booking {
                return BookingDataMapper.mapBookingResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<BookingResponse>> {
                return remoteDataSource.getDetailBooking(id)
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

    override fun payBooking(id: String, transactionProof: String): Flow<Resource<Booking>> {
        return object : NetworkBoundResource<Booking, PayBookingResponse>() {
            override suspend fun fetchFromApi(response: PayBookingResponse): Booking {
                return BookingDataMapper.mapPayBookingResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<PayBookingResponse>> {
                return remoteDataSource.payBooking(id, transactionProof)
            }

        }.asFlow()
    }

    override fun applyDiscount(id: String, discountCode: String): Flow<Resource<Booking>> {
        return object : NetworkBoundResource<Booking, PayBookingResponse>() {
            override suspend fun fetchFromApi(response: PayBookingResponse): Booking {
                return BookingDataMapper.mapPayBookingResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<PayBookingResponse>> {
                return remoteDataSource.applyDiscount(id, discountCode)
            }

        }.asFlow()
    }

    override fun getPagingListBookingByHotel(
        hotelId: String,
        filterDate: String,
        isFinished: Boolean,
        visitorName: String
    ): Flow<PagingData<Booking>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ListBookingPagingSource(
                    apiService,
                    hotelId,
                    filterDate,
                    isFinished,
                    "hotel",
                    visitorName
                )
            }
        ).flow
    }

    override fun getPagingListBookingByVisitor(
        visitorId: String,
        filterDate: String,
        isFinished: Boolean
    ): Flow<PagingData<Booking>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ListBookingPagingSource(apiService, visitorId, filterDate, false, "visitor")
            }
        ).flow
    }

    override fun getPagingListBookingByRoom(
        roomId: String,
        filterDate: String,
        isFinished: Boolean
    ): Flow<PagingData<Booking>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ListBookingPagingSource(apiService, roomId, filterDate, false, "room")
            }
        ).flow
    }
}