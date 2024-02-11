package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.hotel.ListHotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ListHotelResultItem
import com.project.acehotel.core.domain.hotel.model.ManageHotel

object HotelDataMapper {

    fun mapListListHotelResponseToDomain(input: ListHotelResponse): List<ManageHotel> =
        input.result?.map { listHotel ->
            ManageHotel(
                id = listHotel?.id ?: "Empty",
                name = listHotel?.name ?: "Empty",
                address = listHotel?.address ?: "Empty",
                contact = listHotel?.contact ?: "Empty",

                regularRoomCount = listHotel?.regularRoomCount ?: 0,
                regularRoomImage = listHotel?.regularRoomImagePath?.first() ?: "Empty",
                exclusiveRoomCount = listHotel?.exclusiveRoomCount ?: 0,
                exclusiveRoomImage = listHotel?.exclusiveRoomImagePath?.first() ?: "Empty",
                regularRoomPrice = listHotel?.regularRoomPrice ?: 0,
                exclusiveRoomPrice = listHotel?.exclusiveRoomPrice ?: 0,
                extraBedPrice = listHotel?.extraBedPrice ?: 0,

                roomId = (listHotel?.roomId ?: listOf("")) as List<String>,
                inventoryId = (listHotel?.inventoryId ?: listOf("")) as List<String>,

                ownerName = listHotel?.ownerId?.username ?: "Empty",
                ownerEmail = listHotel?.ownerId?.email ?: "Empty",

                receptionistName = listHotel?.receptionistId?.username ?: "Empty",
                receptionistEmail = listHotel?.receptionistId?.email ?: "Empty",

                cleaningStaffName = listHotel?.cleaningStaffId?.username ?: "Empty",
                cleaningStaffEmail = listHotel?.cleaningStaffId?.email ?: "Empty",

                inventoryStaffName = listHotel?.inventoryStaffId?.username ?: "Empty",
                inventoryStaffEmail = listHotel?.inventoryStaffId?.email ?: "Empty",
            )
        } ?: listOf()

    fun mapListHotelResponseToDomain(input: ListHotelResultItem): ManageHotel = ManageHotel(
        id = input?.id ?: "Empty",
        name = input?.name ?: "Empty",
        address = input?.address ?: "Empty",
        contact = input?.contact ?: "Empty",

        regularRoomCount = input?.regularRoomCount ?: 0,
        regularRoomImage = input?.regularRoomImagePath?.first() ?: "Empty",
        exclusiveRoomCount = input?.exclusiveRoomCount ?: 0,
        exclusiveRoomImage = input?.exclusiveRoomImagePath?.first() ?: "Empty",
        regularRoomPrice = input?.regularRoomPrice ?: 0,
        exclusiveRoomPrice = input?.exclusiveRoomPrice ?: 0,
        extraBedPrice = input?.extraBedPrice ?: 0,

        roomId = (input?.roomId ?: listOf("")) as List<String>,
        inventoryId = (input?.inventoryId ?: listOf("")) as List<String>,

        ownerName = input?.ownerId?.username ?: "Empty",
        ownerEmail = input?.ownerId?.email ?: "Empty",

        receptionistName = input?.receptionistId?.username ?: "Empty",
        receptionistEmail = input?.receptionistId?.email ?: "Empty",

        cleaningStaffName = input?.cleaningStaffId?.username ?: "Empty",
        cleaningStaffEmail = input?.cleaningStaffId?.email ?: "Empty",

        inventoryStaffName = input?.inventoryStaffId?.username ?: "Empty",
        inventoryStaffEmail = input?.inventoryStaffId?.email ?: "Empty",
    )

//    fun mapHotelResponseToDomain(input: HotelResponse): Hotel = Hotel(
//        id = input.id ?: "Empty",
//        name = input.name ?: "Empty",
//        address = input.address ?: "Empty",
//        contact = input.contact ?: "Empty",
//
//        regularRoomCount = input.regularRoomCount ?: 0,
//        regularRoomImage = input.regularRoomImagePath?.first() ?: "Empty",
//        exclusiveRoomCount = input.exclusiveRoomCount ?: 0,
//        exclusiveRoomImage = input.exclusiveRoomImagePath?.first() ?: "Empty",
//        regularRoomPrice = input.regularRoomPrice ?: 0,
//        exclusiveRoomPrice = input.exclusiveRoomPrice ?: 0,
//        extraBedPrice = input.extraBedPrice ?: 0,
//
//        roomId = (input.roomId ?: listOf("")) as List<String>,
//        inventoryId = (input.inventoryId ?: listOf("")) as List<String>,
//
//        ownerId = input.ownerId ?: "Empty",
//        receptionistId = input.receptionistId ?: "Empty",
//        cleaningStaffId = input.cleaningStaffId ?: "Empty",
//        inventoryStaffId = input.inventoryStaffId ?: "Empty",
//    )
//
//    fun mapListHotelResponseToDomain(input: ListHotelResponse): List<Hotel> =
//        input.result?.map { hotel ->
//            Hotel(
//                id = hotel?.id ?: "Empty",
//                name = hotel?.name ?: "Empty",
//                address = hotel?.address ?: "Empty",
//                contact = hotel?.contact ?: "Empty",
//
//                regularRoomCount = hotel?.regularRoomCount ?: 0,
//                regularRoomImage = hotel?.regularRoomImagePath?.first() ?: "Empty",
//                exclusiveRoomCount = hotel?.exclusiveRoomCount ?: 0,
//                exclusiveRoomImage = hotel?.exclusiveRoomImagePath?.first() ?: "Empty",
//                regularRoomPrice = hotel?.regularRoomPrice ?: 0,
//                exclusiveRoomPrice = hotel?.exclusiveRoomPrice ?: 0,
//                extraBedPrice = hotel?.extraBedPrice ?: 0,
//
//                roomId = (hotel?.roomId ?: listOf("")) as List<String>,
//                inventoryId = (hotel?.inventoryId ?: listOf("")) as List<String>,
//
//                ownerId = hotel?.ownerId ?: "Empty",
//                receptionistId = hotel?.receptionistId ?: "Empty",
//                cleaningStaffId = hotel?.cleaningStaffId ?: "Empty",
//                inventoryStaffId = hotel?.inventoryStaffId ?: "Empty",
//            )
//        } ?: listOf()
}