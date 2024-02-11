package com.project.acehotel.core.domain.hotel.repository

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.ListHotel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface IHotelRepository {

    fun addHotel(
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
    ): Flow<Resource<ListHotel>>

    fun getListHotel(): Flow<Resource<List<ListHotel>>>

    fun updateHotel(
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
    ): Flow<Resource<ListHotel>>

    fun getSelectedHotel(): Flow<String>

    suspend fun saveSelectedHotel(id: String)

//    fun delete(
//        id: String
//    ): Flow<Resource<Int>>
}