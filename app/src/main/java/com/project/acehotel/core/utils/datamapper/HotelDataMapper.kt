package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.hotel.HotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ListHotelResponse
import com.project.acehotel.core.domain.hotel.model.Hotel

object HotelDataMapper {

    fun mapHotelResponseToDomain(input: HotelResponse): Hotel = Hotel(
        id = input.id ?: "Empty",
        name = input.name ?: "Empty",
        address = input.address ?: "Empty",
        contact = input.contact ?: "Empty",

        regularRoomCount = input.regularRoomCount ?: 0,
        regularRoomImage = input.regularRoomImagePath?.first() ?: "Empty",
        exclusiveRoomCount = input.exclusiveRoomCount ?: 0,
        exclusiveRoomImage = input.exclusiveRoomImagePath?.first() ?: "Empty",
        regularRoomPrice = input.regularRoomPrice ?: 0,
        exclusiveRoomPrice = input.exclusiveRoomPrice ?: 0,
        extraBedPrice = input.extraBedPrice ?: 0,

        roomId = (input.roomId ?: listOf("")) as List<String>,
        inventoryId = (input.inventoryId ?: listOf("")) as List<String>,

        ownerId = input.ownerId ?: "Empty",
        receptionistId = input.receptionistId ?: "Empty",
        cleaningStaffId = input.cleaningStaffId ?: "Empty",
        inventoryStaffId = input.inventoryStaffId ?: "Empty",
    )

    fun mapListHotelResponseToDomain(input: ListHotelResponse): List<Hotel> =
        input.results?.map { hotel ->
            Hotel(
                id = hotel?.id ?: "Empty",
                name = hotel?.name ?: "Empty",
                address = hotel?.address ?: "Empty",
                contact = hotel?.contact ?: "Empty",

                regularRoomCount = hotel?.regularRoomCount ?: 0,
                regularRoomImage = hotel?.regularRoomImagePath?.first() ?: "Empty",
                exclusiveRoomCount = hotel?.exclusiveRoomCount ?: 0,
                exclusiveRoomImage = hotel?.exclusiveRoomImagePath?.first() ?: "Empty",
                regularRoomPrice = hotel?.regularRoomPrice ?: 0,
                exclusiveRoomPrice = hotel?.exclusiveRoomPrice ?: 0,
                extraBedPrice = hotel?.extraBedPrice ?: 0,

                roomId = (hotel?.roomId ?: listOf("")) as List<String>,
                inventoryId = (hotel?.inventoryId ?: listOf("")) as List<String>,

                ownerId = hotel?.ownerId ?: "Empty",
                receptionistId = hotel?.receptionistId ?: "Empty",
                cleaningStaffId = hotel?.cleaningStaffId ?: "Empty",
                inventoryStaffId = hotel?.inventoryStaffId ?: "Empty",
            )
        } ?: listOf()
}