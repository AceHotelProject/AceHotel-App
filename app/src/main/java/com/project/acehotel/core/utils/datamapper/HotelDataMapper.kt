package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.hotel.*
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.hotel.model.Hotel
import com.project.acehotel.core.domain.hotel.model.HotelRecap
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.domain.room.model.Facility
import com.project.acehotel.core.domain.room.model.Room
import com.project.acehotel.core.utils.mapUserRole

object HotelDataMapper {

    fun mapListManageHotelResponseToDomain(input: ManageHotelResponse): List<ManageHotel> =
        input.result?.map { listHotel ->
            ManageHotel(
                id = listHotel?.id ?: "Empty",
                name = listHotel?.name ?: "Empty",
                address = listHotel?.address ?: "Empty",
                contact = listHotel?.contact ?: "Empty",

                regularRoomCount = listHotel?.regularRoomCount ?: 0,
                regularRoomImage = listHotel?.regularRoomImagePath?.firstOrNull()
                    ?: PLACEHOLDER_IMAGE,
                exclusiveRoomCount = listHotel?.exclusiveRoomCount ?: 0,
                exclusiveRoomImage = listHotel?.exclusiveRoomImagePath?.firstOrNull()
                    ?: PLACEHOLDER_IMAGE,
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

                discountAmount = listHotel?.discountAmount ?: 0,
                discount = listHotel?.discountCode ?: "Empty",
            )
        } ?: listOf()

    fun mapManageHotelResponseToDomain(input: ManageHotelResultItem): ManageHotel = ManageHotel(
        id = input.id ?: "Empty",
        name = input.name ?: "Empty",
        address = input.address ?: "Empty",
        contact = input.contact ?: "Empty",

        regularRoomCount = input.regularRoomCount ?: 0,
        regularRoomImage = input.regularRoomImagePath?.firstOrNull() ?: PLACEHOLDER_IMAGE,
        exclusiveRoomCount = input.exclusiveRoomCount ?: 0,
        exclusiveRoomImage = input.exclusiveRoomImagePath?.firstOrNull() ?: PLACEHOLDER_IMAGE,
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

        discountAmount = input.discountAmount ?: 0,
        discount = input.discountCode ?: "Empty",
    )

    fun mapCreateHotelResponseToDomain(input: CreateHotelResponse): ManageHotel = ManageHotel(
        id = input.id ?: "Empty",
        name = input.name ?: "Empty",
        address = input.address ?: "Empty",
        contact = input.contact ?: "Empty",

        regularRoomCount = input.regularRoomCount ?: 0,
        regularRoomImage = input.regularRoomImagePath?.firstOrNull() ?: PLACEHOLDER_IMAGE,
        exclusiveRoomCount = input.exclusiveRoomCount ?: 0,
        exclusiveRoomImage = input.exclusiveRoomImagePath?.firstOrNull() ?: PLACEHOLDER_IMAGE,
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

        discountAmount = 0,
        discount = "Empty",
    )

    fun mapHotelResponseToDomain(input: HotelResponse): Hotel = Hotel(
        id = input.id ?: "Empty",

        name = input.name ?: "Empty",
        address = input.address ?: "Empty",
        contact = input.contact ?: "Empty",

        regularRoomCount = input.regularRoomCount ?: 0,
        regularRoomImage = input.regularRoomImagePath?.firstOrNull() ?: PLACEHOLDER_IMAGE,

        exclusiveRoomCount = input.exclusiveRoomCount ?: 0,
        exclusiveRoomImage = input.exclusiveRoomImagePath?.firstOrNull() ?: PLACEHOLDER_IMAGE,

        regularRoomPrice = input.regularRoomPrice ?: 0,
        exclusiveRoomPrice = input.exclusiveRoomPrice ?: 0,
        extraBedPrice = input.extraBedPrice ?: 0,

        discountAmount = input.extraBedPrice ?: 0,
        discountCode = input.discountCode ?: "Empty",


        roomId = input.roomId?.map { room ->
            Room(
                facility = Facility(
                    bantalHitam = room?.facility?.bantalHitam ?: false,
                    bantalPutih = room?.facility?.bantalPutih ?: false,
                    tv = room?.facility?.tv ?: false,
                    remoteTV = room?.facility?.remoteTv ?: false,
                    remoteAC = room?.facility?.remoteAc ?: false,
                    gantunganBaju = room?.facility?.gantunganBaju ?: false,
                    karpet = room?.facility?.karpet ?: false,
                    cerminWastafel = room?.facility?.cerminWastafel ?: false,
                    shower = room?.facility?.shower ?: false,
                    selendang = room?.facility?.selendang ?: false,
                    keranjangSampah = room?.facility?.kerangjangSampah ?: false,
                    kursi = room?.facility?.kursi ?: false,
                ),
                type = room?.type ?: "Empty",
                hotelId = room?.hotelId ?: "Empty",
                price = room?.price ?: 0,
                isBooked = room?.isBooked ?: false,
                isClean = room?.isClean ?: false,
                name = room?.name ?: "Empty",

                bookings = room?.bookings?.map { booking ->
                    Booking(
                        addOn = listOf(),
                        isProofUploaded = false,
                        transactionProof = "",
                        hotelId = "Empty",
                        visitorId = "Empty",
                        checkinDate = "Empty",
                        checkoutDate = "Empty",
                        duration = 0,
                        roomCount = 0,
                        totalPrice = 0,
                        type = "Empty",
                        id = "Empty",
                        visitorName = "Empty",
                        room = listOf()
                    )
                } ?: listOf(),

                id = room?.id ?: "Empty",
            )
        } ?: listOf(),
        cleaningStaff = User(
            role = mapUserRole(input.cleaningStaffId?.role ?: "role"),
            username = input.cleaningStaffId?.username ?: "Empty",
            email = input.cleaningStaffId?.email ?: "Empty",
            id = input.cleaningStaffId?.id ?: "Empty",
            hotelId = (input.ownerId?.hotelId ?: listOf()) as List<String>
        ),
        receptionist = User(
            role = mapUserRole(input.receptionistId?.role ?: "role"),
            username = input.receptionistId?.username ?: "Empty",
            email = input.receptionistId?.email ?: "Empty",
            id = input.receptionistId?.id ?: "Empty",
            hotelId = (input.ownerId?.hotelId ?: listOf()) as List<String>
        ),
        inventoryStaff = User(
            role = mapUserRole(input.inventoryStaffId?.role ?: "role"),
            username = input.inventoryStaffId?.username ?: "Empty",
            email = input.inventoryStaffId?.email ?: "Empty",
            id = input.inventoryStaffId?.id ?: "Empty",
            hotelId = (input.ownerId?.hotelId ?: listOf()) as List<String>
        ),
        owner = User(
            role = mapUserRole(input.ownerId?.role ?: "role"),
            username = input.ownerId?.username ?: "Empty",
            email = input.ownerId?.email ?: "Empty",
            id = input.ownerId?.id ?: "Empty",
            hotelId = (input.ownerId?.hotelId ?: listOf()) as List<String>
        ),
        inventoryId = (input.inventoryId ?: listOf()) as List<String>
    )

    fun mapHotelRecapResponseToDomain(input: HotelRecapResponse): HotelRecap = HotelRecap(
        revenue = input.revenue ?: 0,
        branchCount = input.branchCount ?: 0,
        checkinCount = input.checkinCount ?: 0,
        totalBooking = input.totalBooking ?: 0,
    )

    fun mapHotelToManageHotel(input: Hotel?): ManageHotel = ManageHotel(
        id = input?.id ?: "Empty",
        name = input?.name ?: "Empty",
        address = input?.address ?: "Empty",
        contact = input?.contact ?: "Empty",

        regularRoomImage = input?.regularRoomImage ?: "Empty",
        exclusiveRoomImage = input?.exclusiveRoomImage ?: "Empty",
        regularRoomCount = input?.regularRoomCount ?: 0,
        exclusiveRoomCount = input?.exclusiveRoomCount ?: 0,
        regularRoomPrice = input?.regularRoomPrice ?: 0,
        exclusiveRoomPrice = input?.exclusiveRoomPrice ?: 0,
        extraBedPrice = input?.extraBedPrice ?: 0,

        discount = input?.discountCode ?: "Empty",
        discountAmount = input?.discountAmount ?: 0,

        ownerId = input?.owner?.id ?: "",
        ownerName = input?.owner?.username ?: "",
        ownerEmail = input?.owner?.email ?: "",

        receptionistId = input?.receptionist?.id ?: "",
        receptionistName = input?.receptionist?.username ?: "",
        receptionistEmail = input?.receptionist?.email ?: "",

        cleaningStaffId = input?.cleaningStaff?.id ?: "",
        cleaningStaffName = input?.cleaningStaff?.username ?: "",
        cleaningStaffEmail = input?.cleaningStaff?.email ?: "",

        inventoryStaffId = input?.inventoryStaff?.id ?: "",
        inventoryStaffName = input?.inventoryStaff?.username ?: "",
        inventoryStaffEmail = input?.inventoryStaff?.email ?: "",
    )

    private const val PLACEHOLDER_IMAGE =
        "https://storage.googleapis.com/ace-hotel/placeholder_image.png"
}