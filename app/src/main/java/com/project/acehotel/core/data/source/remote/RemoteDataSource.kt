package com.project.acehotel.core.data.source.remote

import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.network.ApiService
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.data.source.remote.response.auth.RefreshTokenResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ListHotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ListHotelResultItem
import com.project.acehotel.core.data.source.remote.response.images.UploadImagesResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
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

    // AUTH

    // INVENTORY

    suspend fun getListInventory(hotelId: String): Flow<ApiResponse<InventoryListResponse>> {
        return flow<ApiResponse<InventoryListResponse>> {
            try {
                val response = apiService.getListInventory(hotelId)

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

    suspend fun addInventory(
        name: String,
        type: String,
        stock: Int
    ): Flow<ApiResponse<InventoryDetailResponse>> {
        return flow {
            try {
                val response = apiService.addInventory(name, type, stock)

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
        name: String,
        type: String,
        stock: Int,
        title: String,
        description: String
    ): Flow<ApiResponse<InventoryDetailResponse>> {
        return flow {
            try {
                val response = apiService.updateInventory(id, name, type, stock, title, description)

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

    suspend fun deleteInventory(id: String): Flow<ApiResponse<Response<InventoryDetailResponse>>> {
        return flow<ApiResponse<Response<InventoryDetailResponse>>> {
            try {
                val response = apiService.deleteInventory(id)

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

    // INVENTORY

    // HOTEL

    suspend fun addHotel(
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
        inventoryPassword: String,
    ): Flow<ApiResponse<ListHotelResultItem>> {
        return flow<ApiResponse<ListHotelResultItem>> {
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

    suspend fun getListHotel(): Flow<ApiResponse<ListHotelResponse>> {
        return flow<ApiResponse<ListHotelResponse>> {
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
        inventoryPassword: String,
    ): Flow<ApiResponse<ListHotelResultItem>> {
        return flow<ApiResponse<ListHotelResultItem>> {
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

//    suspend fun deleteHotel(id: String): Flow<ApiResponse<Response<HotelResponse>>> {
//        return flow<ApiResponse<Response<HotelResponse>>> {
//            try {
//                val response = apiService.deleteHotel(id)
//
//                if (response.isSuccessful) {
//                    emit(ApiResponse.Success(response))
//                } else {
//                    emit(ApiResponse.Empty)
//                }
//            } catch (e: Exception) {
//                emit(ApiResponse.Error(e.toString()))
//                Timber.tag("RemoteDataSource").e(e.toString())
//            }
//        }.flowOn(Dispatchers.IO)
//    }

    // HOTEL

    // VISITOR

    suspend fun getListVisitor(): Flow<ApiResponse<ListVisitorResponse>> {
        return flow<ApiResponse<ListVisitorResponse>> {
            try {
                val response = apiService.getListVisitor()

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


    // VISITOR

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