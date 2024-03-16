package com.project.acehotel.core.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.acehotel.core.data.source.remote.network.ApiService
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.utils.datamapper.BookingDataMapper
import timber.log.Timber
import javax.inject.Inject

class ListBookingPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val id: String,
    private val filterDate: String,
) : PagingSource<Int, Booking>() {
    override fun getRefreshKey(state: PagingState<Int, Booking>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Booking> {
        return try {
            val position = params.key ?: START_PAGE_INDEX

            Timber.tag("ListBooking").e("$position + ${params.loadSize}")
            val response =
                apiService.getPagingListBookingByHotel(id, filterDate, position, params.loadSize)
            val listBooking = BookingDataMapper.mapListBookingToDomain(response)

            LoadResult.Page(
                data = listBooking,
                prevKey = if (position == START_PAGE_INDEX) null else position - 1,
                nextKey = if (listBooking.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        private const val START_PAGE_INDEX = 1
    }

}

//class ListBookingPagingSource @Inject constructor(
//    private val apiService: ApiService
//    private val id: String,
//    private val filterDate: String
//) : PagingSource<Int, List<Booking>>() {
//
//    override fun getRefreshKey(state: PagingState<Int, List<Booking>>): Int? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, List<Booking>> {
//        return try {
//            val position = params.key ?: START_PAGE_INDEX
//            val response = apiService.getPagingListBookingByHotel(id, filterDate, position, params.loadSize)
//
//            val listBooking = BookingDataMapper.mapListBookingToDomain(response)
//            LoadResult.Page(data = listBooking)
//
//        }
////        val currentPage = params.key ?: START_PAGE_INDEX
////
////        return try {
////            val response = remoteDataSource.getListBookingByHotel(id, filterDate).first()
////            when (response) {
////                is ApiResponse.Success -> {
////                    val listBooking = response.data.results
////                    val nextPageNumber = if (bookings.isEmpty()) null else currentPage + 1
////                    LoadResult.Page(
////                        data = listBooking,
////                        prevKey = if (currentPage == START_PAGE_INDEX) null else currentPage - 1,
////                        nextKey = if (listBooking.isEmpty()) null else currentPage + 1
////                    )
////                }
////                is ApiResponse.Error -> {
////                    LoadResult.Error(Exception(response.errorMessage))
////                }
////                is ApiResponse.Empty -> {
////                    LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
////                }
////            }
////        } catch (exception: Exception) {
////            LoadResult.Error(exception)
////        }
//    }
//
////    protected suspend fun fetchFromApi(response: ListBookingResponse): List<Booking> {
////        return BookingDataMapper.mapListBookingToDomain(response)
////    }
//
//    companion object {
//        private const val START_PAGE_INDEX = 1
//    }
//}