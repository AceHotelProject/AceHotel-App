package com.project.acehotel.core.data.source.remote

import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.network.ApiService
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.data.source.remote.response.auth.RefreshTokenResponse
import com.project.acehotel.core.data.source.remote.response.booking.AddBookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.BookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.ListBookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.PayBookingResponse
import com.project.acehotel.core.data.source.remote.response.hotel.*
import com.project.acehotel.core.data.source.remote.response.images.UploadImagesResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryUpdateHistoryItem
import com.project.acehotel.core.data.source.remote.response.note.NoteResponse
import com.project.acehotel.core.data.source.remote.response.room.CheckoutBody
import com.project.acehotel.core.data.source.remote.response.room.ListRoomResponse
import com.project.acehotel.core.data.source.remote.response.room.RoomResponse
import com.project.acehotel.core.data.source.remote.response.tag.*
import com.project.acehotel.core.data.source.remote.response.user.ListUserResponse
import com.project.acehotel.core.data.source.remote.response.user.UserResponse
import com.project.acehotel.core.data.source.remote.response.visitor.ListVisitorResponse
import com.project.acehotel.core.data.source.remote.response.visitor.VisitorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import retrofit2.Response
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
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.tag("RemoteDataSource").e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getUserByHotel(hotelId: String): Flow<ApiResponse<ListUserResponse>> {
        return flow {
            try {
                val response = apiService.getUserByHotel(hotelId)

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

    fun getUserById(id: String, hotelId: String): Flow<ApiResponse<UserResponse>> {
        return flow<ApiResponse<UserResponse>> {
            try {
                val response = apiService.getUserById(id, hotelId)

                if (!response.id.isNullOrEmpty()) {
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

    fun updateUser(
        id: String,
        hotelId: String,
        email: String,
    ): Flow<ApiResponse<UserResponse>> {
        return flow {
            try {
                val response = apiService.updateUser(id, hotelId, email)

                if (!response.id.isNullOrEmpty()) {
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

    suspend fun deleteUser(id: String, hotelId: String): Flow<ApiResponse<Response<UserResponse>>> {
        return flow {
            try {
                val response = apiService.deleteUser(id, hotelId)

                if (!response.isSuccessful) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.tag("RemoteDataSource").e(e.toString())
            }
        }
    }

    suspend fun forgetPassword(email: String): Flow<ApiResponse<Response<AuthResponse>>> {
        return flow<ApiResponse<Response<AuthResponse>>> {
            try {
                val response = apiService.forgetPassword(email)

                if (response.isSuccessful) {
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

    // AUTH

    // INVENTORY

    suspend fun getListInventory(
        hotelId: String,
        name: String,
        type: String
    ): Flow<ApiResponse<InventoryListResponse>> {
        return flow<ApiResponse<InventoryListResponse>> {
            try {
                val inventoryFilters = mutableMapOf<String, String>()
                if (name.isNotEmpty()) {
                    inventoryFilters["name"] = name
                }
                if (type.isNotEmpty()) {
                    inventoryFilters["type"] = type
                }

                val response = apiService.getListInventory(hotelId, inventoryFilters)

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
        id: String,
        hotelId: String
    ): Flow<ApiResponse<InventoryDetailResponse>> {
        return flow<ApiResponse<InventoryDetailResponse>> {
            try {
                val response = apiService.getDetailInventory(id, hotelId)

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

    suspend fun getInventoryHistoryList(
        id: String,
        key: String
    ): Flow<ApiResponse<List<InventoryUpdateHistoryItem?>?>> {
        return flow<ApiResponse<List<InventoryUpdateHistoryItem?>?>> {
            try {
                val response = apiService.getInventoryHistoryList(id, key)

                if (!response.isNullOrEmpty()) {
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

    suspend fun addInventory(
        hotelId: String,
        name: String,
        type: String,
        stock: Int
    ): Flow<ApiResponse<InventoryDetailResponse>> {
        return flow {
            try {
                val response = apiService.addInventory(hotelId, name, type, stock)

                if (response.id != null) {
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

    suspend fun updateInventory(
        id: String,
        hotelId: String,
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ): Flow<ApiResponse<InventoryDetailResponse>> {
        return flow {
            try {
                val response =
                    apiService.updateInventory(id, hotelId, name, type, stock, title, description)

                if (response.id != null) {
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

    suspend fun deleteInventory(
        inventoryId: String,
        hotelId: String
    ): Flow<ApiResponse<Response<InventoryDetailResponse>>> {
        return flow<ApiResponse<Response<InventoryDetailResponse>>> {
            try {
                val response = apiService.deleteInventory(inventoryId, hotelId)

                if (response.isSuccessful) {
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

    suspend fun getTagById(
        readerId: String
    ): Flow<ApiResponse<ListTagsByIdResponse>> {
        return flow<ApiResponse<ListTagsByIdResponse>> {
            try {
                val response = apiService.getTagById(readerId)

                if (!response.tagId.isNullOrEmpty()) {
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

    suspend fun getTag(): Flow<ApiResponse<ListTagsResponse>> {
        return flow<ApiResponse<ListTagsResponse>> {
            try {
                val response = apiService.getTag()

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

    suspend fun addTag(
        tid: String,
        inventoryId: String,
    ): Flow<ApiResponse<AddTagResponse>> {
        return flow<ApiResponse<AddTagResponse>> {
            try {
                val response = apiService.addTag(tid, inventoryId)

                if (response.id != null) {
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

    suspend fun setQueryTag(
        readerId: String,
        state: Boolean
    ): Flow<ApiResponse<ReaderQueryResponse>> {
        return flow<ApiResponse<ReaderQueryResponse>> {
            try {
                val response = apiService.setQueryTag(readerId, state)

                if (response.params != null) {
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

    suspend fun updateReader(
        readerId: String,
        powerGain: Int,
        readInterval: Int
    ): Flow<ApiResponse<ReaderResponse>> {
        return flow<ApiResponse<ReaderResponse>> {
            try {
                val response =
                    apiService.updateReader(readerId, powerGain.toString(), readInterval.toString())

                if (response.id != null) {
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


    suspend fun getReader(readerId: String): Flow<ApiResponse<ReaderResponse>> {
        return flow<ApiResponse<ReaderResponse>> {
            try {
                val response = apiService.getReader(readerId)

                if (response.id != null) {
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

    // HOTEL

    suspend fun addHotel(
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
        inventoryPassword: String,
    ): Flow<ApiResponse<CreateHotelResponse>> {
        return flow<ApiResponse<CreateHotelResponse>> {
            try {
                val response =
                    apiService.addHotel(
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

                if (response.ownerId != null) {
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

    suspend fun getHotel(id: String): Flow<ApiResponse<HotelResponse>> {
        return flow<ApiResponse<HotelResponse>> {
            try {
                val response = apiService.getHotel(id)

                if (response.id != null) {
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

    suspend fun getListHotel(): Flow<ApiResponse<ManageHotelResponse>> {
        return flow<ApiResponse<ManageHotelResponse>> {
            try {
                val response = apiService.getListHotel()

                if (response.result != null) {
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

    suspend fun updateHotel(
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

        ): Flow<ApiResponse<HotelResponse>> {
        return flow<ApiResponse<HotelResponse>> {
            try {
                val response = apiService.updateHotel(
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
                )

                if (response.id != null) {
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

    suspend fun updateHotelPrice(
        id: String,
        discountCode: String,
        discountAmount: Int,
        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int
    ): Flow<ApiResponse<HotelResponse>> {
        return flow<ApiResponse<HotelResponse>> {
            try {
                val response = apiService.updateHotelPrice(
                    id,
                    discountCode,
                    discountAmount,
                    regularRoomPrice,
                    exclusiveRoomPrice,
                    extraBedPrice
                )

                if (response.id != null) {
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

    suspend fun deleteHotel(id: String): Flow<ApiResponse<Response<ManageHotelResultItem>>> {
        return flow<ApiResponse<Response<ManageHotelResultItem>>> {
            try {
                val response = apiService.deleteHotel(id)

                if (response.isSuccessful) {
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

    suspend fun getHotelRecap(
        filterDate: String,
    ): Flow<ApiResponse<HotelRecapResponse>> {
        return flow<ApiResponse<HotelRecapResponse>> {
            try {
                val response = apiService.getHotelRecap(filterDate)

                if (response.revenue != null) {
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

    // HOTEL

    // VISITOR

    suspend fun getListVisitor(
        hotelId: String,
        name: String,
        email: String,
        identityNum: String
    ): Flow<ApiResponse<ListVisitorResponse>> {
        return flow<ApiResponse<ListVisitorResponse>> {
            try {
                val visitorFilters = mutableMapOf<String, String>()
                if (hotelId.isNotEmpty()) {
                    visitorFilters["hotel_id"] = hotelId
                }
                if (name.isNotEmpty()) {
                    visitorFilters["name"] = name
                }
                if (email.isNotEmpty()) {
                    visitorFilters["email"] = email
                }
                if (identityNum.isNotEmpty()) {
                    visitorFilters["identity_num"] = identityNum
                }

                val response = apiService.getListVisitor(visitorFilters)

                if (response.results != null) {
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

    suspend fun getDetailVisitor(id: String): Flow<ApiResponse<VisitorResponse>> {
        return flow<ApiResponse<VisitorResponse>> {
            try {
                val response = apiService.getDetailVisitor(id)

                if (response.id != null) {
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

    suspend fun addVisitor(
        hotelId: String,
        name: String,
        address: String,
        phone: String,
        email: String,
        identityNum: String,
        pathIdentityImage: String
    ): Flow<ApiResponse<VisitorResponse>> {
        return flow {
            try {
                val response = apiService.addVisitor(
                    hotelId,
                    name,
                    address,
                    phone,
                    email,
                    identityNum,
                    pathIdentityImage
                )

                if (response.id != null) {
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

    suspend fun updateVisitor(
        id: String,
        hotelId: String,
        name: String,
        address: String,
        phone: String,
        email: String,
        identityNum: String,
        pathIdentityImage: String
    ): Flow<ApiResponse<VisitorResponse>> {
        return flow {
            try {
                val response = apiService.updateVisitor(
                    id,
                    hotelId,
                    name,
                    address,
                    phone,
                    email,
                    identityNum,
                    pathIdentityImage
                )

                if (response.id != null) {
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

    suspend fun deleteVisitor(
        id: String,
        hotelId: String
    ): Flow<ApiResponse<Response<VisitorResponse>>> {
        return flow {
            try {
                val response = apiService.deleteVisitor(id, hotelId)

                if (response.isSuccessful) {
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

    // VISITOR


    // BOOKING

    suspend fun addBooking(
        hotelId: String,
        visitorId: String,
        checkinDate: String,
        duration: Int,
        roomCount: Int,
        extraBed: Int,
        type: String,
    ): Flow<ApiResponse<AddBookingResponse>> {
        return flow<ApiResponse<AddBookingResponse>> {
            try {
                val response = apiService.addBooking(
                    hotelId,
                    visitorId,
                    checkinDate,
                    duration,
                    roomCount,
                    extraBed,
                    type
                )

                if (response.id != null) {
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

    suspend fun getListBookingByHotel(
        id: String,
        filterDate: String,
        visitorName: String
    ): Flow<ApiResponse<ListBookingResponse>> {
        return flow {
            try {
                val response = apiService.getListBookingByHotel(id, filterDate, visitorName)

                if (response.results != null) {
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

    suspend fun getListBookingByRoom(
        id: String,
        filterDate: String
    ): Flow<ApiResponse<ListBookingResponse>> {
        return flow {
            try {
                val response = apiService.getListBookingByRoom(id, filterDate)

                if (response.results != null) {
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

    suspend fun getListBookingByVisitor(id: String): Flow<ApiResponse<ListBookingResponse>> {
        return flow<ApiResponse<ListBookingResponse>> {
            try {
                val response = apiService.getListBookingByVisitor(id)

                if (response.results != null) {
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

    suspend fun getDetailBooking(id: String): Flow<ApiResponse<BookingResponse>> {
        return flow<ApiResponse<BookingResponse>> {
            try {
                val response = apiService.getDetailBooking(id)

                if (response.id != null) {
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

    suspend fun deleteBooking(id: String): Flow<ApiResponse<Response<BookingResponse>>> {
        return flow<ApiResponse<Response<BookingResponse>>> {
            try {
                val response = apiService.deleteBooking(id)

                if (response.isSuccessful) {
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

    suspend fun payBooking(
        id: String,
        transactionProof: String
    ): Flow<ApiResponse<PayBookingResponse>> {
        return flow<ApiResponse<PayBookingResponse>> {
            try {
                val response = apiService.payBooking(id, transactionProof)

                if (response.id != null) {
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

    suspend fun applyDiscount(
        id: String,
        discountCode: String
    ): Flow<ApiResponse<PayBookingResponse>> {
        return flow<ApiResponse<PayBookingResponse>> {
            try {
                val response = apiService.addDiscount(id, discountCode)

                if (response.id != null) {
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

    suspend fun getNoteDetail(id: String): Flow<ApiResponse<NoteResponse>> {
        return flow<ApiResponse<NoteResponse>> {
            try {
                val response = apiService.getNoteDetail(id)

                if (!response.bookingId.isNullOrEmpty()) {
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

    // BOOKING


    // ROOM

    suspend fun getListRoomByHotel(id: String): Flow<ApiResponse<ListRoomResponse>> {
        return flow<ApiResponse<ListRoomResponse>> {
            try {
                val response = apiService.getListRoomByHotel(id)

                if (response.results != null) {
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

    suspend fun getRoomDetail(id: String): Flow<ApiResponse<RoomResponse>> {
        return flow<ApiResponse<RoomResponse>> {
            val response = apiService.getRoomDetail(id)

            try {
                if (response.id != null) {
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

    suspend fun roomCheckin(
        id: String,
        checkinDate: String,
        bookingId: String,
        visitorId: String
    ): Flow<ApiResponse<RoomResponse>> {
        return flow<ApiResponse<RoomResponse>> {
            try {
                val response = apiService.roomCheckin(id, checkinDate, bookingId, visitorId)

                if (response.id != null) {
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

    suspend fun roomCheckout(
        id: String,
        checkoutBody: CheckoutBody
    ): Flow<ApiResponse<RoomResponse>> {
        return flow<ApiResponse<RoomResponse>> {
            try {
                val response = apiService.roomCheckout(id, checkoutBody)

                if (response.id != null) {
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

    suspend fun deleteRoom(id: String): Flow<ApiResponse<Response<RoomResponse>>> {
        return flow<ApiResponse<Response<RoomResponse>>> {
            try {
                val response = apiService.deleteRoom(id)

                if (response.isSuccessful) {
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

    // ROOM


    //IMAGES

    suspend fun uploadImage(image: List<MultipartBody.Part>): Flow<ApiResponse<UploadImagesResponse>> {
        return flow<ApiResponse<UploadImagesResponse>> {
            try {
                val response = apiService.uploadImage(image)

                if (response.path != null) {
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

    //IMAGES

}