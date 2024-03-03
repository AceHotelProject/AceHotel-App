package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.room.ListRoomResponse
import com.project.acehotel.core.data.source.remote.response.room.RoomResponse
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.room.model.Facility
import com.project.acehotel.core.domain.room.model.Room

object RoomDataMapper {
    fun mapRoomResponseToDomain(input: RoomResponse): Room = Room(
        facility = Facility(
            bantalHitam = input.facility?.bantalHitam ?: false,
            bantalPutih = input.facility?.bantalPutih ?: false,
            tv = input.facility?.tv ?: false,
            remoteTV = input.facility?.remoteTv ?: false,
            remoteAC = input.facility?.remoteAc ?: false,
            gantunganBaju = input.facility?.gantunganBaju ?: false,
            karpet = input.facility?.karpet ?: false,
            cerminWastafel = input.facility?.cerminWastafel ?: false,
            shower = input.facility?.shower ?: false,
            selendang = input.facility?.selendang ?: false,
            keranjangSampah = input.facility?.kerangjangSampah ?: false,
            kursi = input.facility?.kursi ?: false,
        ),
        type = input.type ?: "Empty",
        hotelId = input.hotelId ?: "Empty",
        price = input.price ?: 0,
        isBooked = input.isBooked ?: false,
        isClean = input.isClean ?: false,
        name = input.name ?: "Empty",

        bookings = input.bookings?.map { booking ->
            Booking(
                roomId = listOf(),
                addOn = listOf(),
                isProofUploaded = false,
                hasProblem = false,
                transactionProof = "",
                noteId = listOf(),
                hotelId = "Empty",
                visitorId = booking?.visitorId ?: "Empty",
                checkinDate = booking?.checkinDate ?: "Empty",
                checkoutDate = booking?.checkoutDate ?: "Empty",
                duration = 0,
                roomCount = 0,
                totalPrice = 0,
                type = "Empty",
                id = booking?.bookingId ?: "Empty",
                visitorName = "Empty",
                actualCheckinDate = "Empty",
                actualCheckoutDate = "Empty",
            )
        } ?: listOf(),

        id = input.id ?: "Empty",
    )

    fun mapListRoomResponseToDomain(input: ListRoomResponse): List<Room> =
        input.results?.map { room ->
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
                        roomId = listOf(),
                        addOn = listOf(),
                        isProofUploaded = false,
                        hasProblem = false,
                        transactionProof = "",
                        noteId = listOf(),
                        hotelId = "Empty",
                        visitorId = booking?.visitorId ?: "Empty",
                        checkinDate = booking?.checkinDate ?: "Empty",
                        checkoutDate = booking?.checkoutDate ?: "Empty",
                        actualCheckinDate = "Empty",
                        actualCheckoutDate = "Empty",
                        duration = 0,
                        roomCount = 0,
                        totalPrice = 0,
                        type = "Empty",
                        id = booking?.bookingId ?: "Empty",
                        visitorName = "Empty"
                    )
                } ?: listOf(),

                id = room?.id ?: "Empty",
            )
        } ?: listOf()
}