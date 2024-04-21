package com.project.acehotel.core.domain.hotel.usecase

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.Hotel
import com.project.acehotel.core.domain.hotel.model.HotelRecap
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import kotlinx.coroutines.flow.Flow

interface HotelUseCase {

    fun addHotel(
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
    ): Flow<Resource<ManageHotel>>

    fun getListHotel(): Flow<Resource<List<ManageHotel>>>

    fun getHotel(id: String): Flow<Resource<Hotel>>

    fun updateHotel(
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
    ): Flow<Resource<Hotel>>

    fun updateHotelPrice(
        id: String,

        discountCode: String,
        discountAmount: Int,

        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int,
    ): Flow<Resource<Hotel>>

    fun deleteHotel(
        id: String
    ): Flow<Resource<Int>>

    fun getSelectedHotelData(): Flow<ManageHotel>

    fun saveSelectedHotelData(data: ManageHotel): Flow<Boolean>

    fun getHotelRecap(filterDate: String): Flow<Resource<HotelRecap>>
}