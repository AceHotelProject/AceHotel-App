package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.hotel.CreateHotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ManageHotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ManageHotelResultItem
import com.project.acehotel.core.domain.hotel.model.ManageHotel

object HotelDataMapper {

    fun mapListManageHotelResponseToDomain(input: ManageHotelResponse): List<ManageHotel> =
        input.result?.map { listHotel ->
            ManageHotel(
                id = listHotel?.id ?: "Empty",
                name = listHotel?.name ?: "Empty",
                address = listHotel?.address ?: "Empty",
                contact = listHotel?.contact ?: "Empty",

                regularRoomCount = listHotel?.regularRoomCount ?: 0,
                regularRoomImage = listHotel?.regularRoomImagePath?.firstOrNull() ?: "Empty",
                exclusiveRoomCount = listHotel?.exclusiveRoomCount ?: 0,
                exclusiveRoomImage = listHotel?.exclusiveRoomImagePath?.firstOrNull() ?: "Empty",
                regularRoomPrice = listHotel?.regularRoomPrice ?: 0,
                exclusiveRoomPrice = listHotel?.exclusiveRoomPrice ?: 0,
                extraBedPrice = listHotel?.extraBedPrice ?: 0,

                roomId = (listHotel?.roomId ?: listOf("")) as List<String>,
                inventoryId = (listHotel?.inventoryId ?: listOf("")) as List<String>,

                ownerId = listHotel?.ownerId?.id ?: "Empty",
                ownerName = listHotel?.ownerId?.username ?: "Empty",
                ownerEmail = listHotel?.ownerId?.email ?: "Empty",

                receptionistId = listHotel?.receptionistId?.id ?: "Empty",
                receptionistName = listHotel?.receptionistId?.username ?: "Empty",
                receptionistEmail = listHotel?.receptionistId?.email ?: "Empty",

                cleaningStaffId = listHotel?.cleaningStaffId?.id ?: "Empty",
                cleaningStaffName = listHotel?.cleaningStaffId?.username ?: "Empty",
                cleaningStaffEmail = listHotel?.cleaningStaffId?.email ?: "Empty",

                inventoryStaffId = listHotel?.inventoryStaffId?.id ?: "Empty",
                inventoryStaffName = listHotel?.inventoryStaffId?.username ?: "Empty",
                inventoryStaffEmail = listHotel?.inventoryStaffId?.email ?: "Empty",
            )
        } ?: listOf()

    fun mapManageHotelResponseToDomain(input: ManageHotelResultItem): ManageHotel = ManageHotel(
        id = input.id ?: "Empty",
        name = input.name ?: "Empty",
        address = input.address ?: "Empty",
        contact = input.contact ?: "Empty",

        regularRoomCount = input.regularRoomCount ?: 0,
        regularRoomImage = input.regularRoomImagePath?.firstOrNull() ?: "Empty",
        exclusiveRoomCount = input.exclusiveRoomCount ?: 0,
        exclusiveRoomImage = input.exclusiveRoomImagePath?.firstOrNull() ?: "Empty",
        regularRoomPrice = input.regularRoomPrice ?: 0,
        exclusiveRoomPrice = input.exclusiveRoomPrice ?: 0,
        extraBedPrice = input.extraBedPrice ?: 0,

        roomId = (input.roomId ?: listOf("")) as List<String>,
        inventoryId = (input.inventoryId ?: listOf("")) as List<String>,

        ownerId = input.ownerId?.id ?: "Empty",
        ownerName = input.ownerId?.username ?: "Empty",
        ownerEmail = input.ownerId?.email ?: "Empty",

        receptionistId = input.receptionistId?.id ?: "Empty",
        receptionistName = input.receptionistId?.username ?: "Empty",
        receptionistEmail = input.receptionistId?.email ?: "Empty",

        cleaningStaffId = input.cleaningStaffId?.id ?: "Empty",
        cleaningStaffName = input.cleaningStaffId?.username ?: "Empty",
        cleaningStaffEmail = input.cleaningStaffId?.email ?: "Empty",

        inventoryStaffId = input.inventoryStaffId?.id ?: "Empty",
        inventoryStaffName = input.inventoryStaffId?.username ?: "Empty",
        inventoryStaffEmail = input.inventoryStaffId?.email ?: "Empty",
    )

    fun mapCreateHotelResponseToDomain(input: CreateHotelResponse): ManageHotel = ManageHotel(
        id = input.id ?: "Empty",
        name = input.name ?: "Empty",
        address = input.address ?: "Empty",
        contact = input.contact ?: "Empty",

        regularRoomCount = input.regularRoomCount ?: 0,
        regularRoomImage = input.regularRoomImagePath?.firstOrNull() ?: "Empty",
        exclusiveRoomCount = input.exclusiveRoomCount ?: 0,
        exclusiveRoomImage = input.exclusiveRoomImagePath?.firstOrNull() ?: "Empty",
        regularRoomPrice = input.regularRoomPrice ?: 0,
        exclusiveRoomPrice = input.exclusiveRoomPrice ?: 0,
        extraBedPrice = input.extraBedPrice ?: 0,

        roomId = (input.roomId ?: listOf("")) as List<String>,
        inventoryId = (input.inventoryId ?: listOf("")) as List<String>,

        ownerId = input.ownerId ?: "Empty",
        ownerName = "Empty",
        ownerEmail = "Empty",

        receptionistId = input.receptionistId ?: "Empty",
        receptionistName = "Empty",
        receptionistEmail = "Empty",

        cleaningStaffId = input.cleaningStaffId ?: "Empty",
        cleaningStaffName = "Empty",
        cleaningStaffEmail = "Empty",

        inventoryStaffId = input.inventoryStaffId ?: "Empty",
        inventoryStaffName = "Empty",
        inventoryStaffEmail = "Empty",
    )
}