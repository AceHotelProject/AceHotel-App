package com.project.acehotel.core.domain.hotel.interactor

import com.project.acehotel.core.data.repository.HotelRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.Hotel
import com.project.acehotel.core.domain.hotel.model.HotelRecap
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HotelInteractor @Inject constructor(private val hotelRepository: HotelRepository) :
    HotelUseCase {
    override fun addHotel(
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
        inventoryPassword: String
    ): Flow<Resource<ManageHotel>> {
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

    override fun getListHotel(): Flow<Resource<List<ManageHotel>>> {
        return hotelRepository.getListHotel()
    }

    override fun getHotel(id: String): Flow<Resource<Hotel>> {
        return hotelRepository.getHotel(id)
    }

    override fun updateHotel(
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
        )
    }

    override fun updateHotelPrice(
        id: String,
        discountCode: String,
        discountAmount: Int,
        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int
    ): Flow<Resource<Hotel>> {
        return hotelRepository.updateHotelPrice(
            id,
            discountCode,
            discountAmount,
            regularRoomPrice,
            exclusiveRoomPrice,
            extraBedPrice
        )
    }

    override fun deleteHotel(id: String): Flow<Resource<Int>> = hotelRepository.deleteHotel(id)

    override fun getSelectedHotelData(): Flow<ManageHotel> = hotelRepository.getSelectedHotelData()

    override fun saveSelectedHotelData(data: ManageHotel): Flow<Boolean> =
        hotelRepository.saveSelectedHotelData(data)

    override fun getHotelRecap(filterDate: String): Flow<Resource<HotelRecap>> =
        hotelRepository.getHotelRecap(filterDate)
}