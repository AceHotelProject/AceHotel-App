package com.project.acehotel.core.domain.hotel.interactor

import com.project.acehotel.core.data.repository.HotelRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.Hotel
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class HotelInteractor @Inject constructor(private val hotelRepository: HotelRepository) :
    HotelUseCase {
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
        return hotelRepository.addHotel(
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

    override fun getHotels(): Flow<Resource<List<Hotel>>> {
        return hotelRepository.getHotels()
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
        return hotelRepository.updateHotel(
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
}