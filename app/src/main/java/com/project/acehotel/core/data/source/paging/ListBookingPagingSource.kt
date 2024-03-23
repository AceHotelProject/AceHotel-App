package com.project.acehotel.core.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.acehotel.core.data.source.remote.network.ApiService
import com.project.acehotel.core.data.source.remote.response.booking.ListBookingResponse
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.utils.constants.FilterDate
import com.project.acehotel.core.utils.datamapper.BookingDataMapper
import timber.log.Timber
import javax.inject.Inject

class ListBookingPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val id: String,
    private val filterDate: String,
    private var isFinished: Boolean = false,
    private val endPoint: String,
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

            val response: ListBookingResponse = when (endPoint) {
                "room" -> {
                    apiService.getPagingListBookingByRoom(
                        id,
                        filterDate,
                        position,
                        params.loadSize
                    )
                }
                "visitor" -> {
                    apiService.getPagingListBookingByVisitor(
                        id,
                        filterDate,
                        position,
                        params.loadSize
                    )
                }
                "hotel" -> {
                    apiService.getPagingListBookingByHotel(
                        id,
                        filterDate,
                        position,
                        params.loadSize
                    )
                }
                else -> {
                    apiService.getPagingListBookingByHotel(
                        id,
                        filterDate,
                        position,
                        params.loadSize
                    )
                }
            }

            val listBooking = BookingDataMapper.mapListBookingToDomain(response)

            when (filterDate) {
                FilterDate.NOW.value -> {
                    listBooking.filter {
                        if (it.room.isNotEmpty()) {
                            it.id == ""
                        } else {
                            it.id.isNotEmpty()
                        }
                    }
                }
                FilterDate.MONTH.value -> {
                    listBooking.filter {
                        if (it.room.isNotEmpty()) {
                            it.room.first().actualCheckin == "Empty" || it.room.first().actualCheckout != "Empty"
                        } else {
                            it.id == ""
                        }
                    }
                }
                FilterDate.YEAR.value -> {
                    if (isFinished) {
                        listBooking.filter {
                            if (it.room.isNotEmpty()) {
                                Timber.tag("TEST").e("Masuk ${it.id}")
                                it.room.first().actualCheckin != "Empty" && it.room.first().actualCheckout != "Empty"
                            } else {
                                it.id == ""
                            }
                        }
                    } else {
                        listBooking.filter {
                            if (it.room.isNotEmpty()) {
                                it.room.first().actualCheckin == "Empty" || it.room.first().actualCheckout != "Empty"
                            } else {
                                it.id == ""
                            }
                        }
                    }
                }
            }
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