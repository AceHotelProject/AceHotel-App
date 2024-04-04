package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.booking.*
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.model.RoomBooking
import timber.log.Timber

object BookingDataMapper {

    fun mapAddBookingResponseToDomain(input: AddBookingResponse): Booking = Booking(
        addOn = (input.addOnId ?: listOf()) as List<String>,
        totalPrice = input.totalPrice ?: 0,
        duration = input.duration ?: 0,
        roomCount = input.roomCount ?: 0,
        isProofUploaded = input.isProofUploaded ?: false,
        hotelId = input.hotelId ?: "Empty",
        visitorId = input.visitorId ?: "Empty",
        checkinDate = input.checkinDate ?: "Empty",
        checkoutDate = input.checkoutDate ?: "Empty",
        type = input.type ?: "Empty",
        id = input.id ?: "Empty",
        transactionProof = "Empty",
        visitorName = "Empty",
        room = input.room?.map {
            RoomBooking(
                id = it?.roomId ?: "Empty",
                actualCheckin = "Empty",
                actualCheckout = "Empty",
                checkoutStaffId = "Empty",
                checkinStaffId = "Empty",
                hasProblem = false,
                note = "Empty",
            )
        } ?: listOf()
    )

    fun mapBookingResponseToDomain(input: BookingResponse): Booking = Booking(
        addOn = (input.addOnId ?: listOf()) as List<String>,
        room = input.room?.map {
            RoomBooking(
                id = it?.roomId ?: "Empty",
                actualCheckin = it?.actualCheckin ?: "Empty",
                actualCheckout = it?.actualCheckout ?: "Empty",
                checkoutStaffId = it?.checkoutStaffId ?: "Empty",
                checkinStaffId = it?.checkinStaffId ?: "Empty",
                hasProblem = false,
                note = "Empty",
            )
        } ?: listOf(),
        totalPrice = input.totalPrice ?: 0,
        duration = input.duration ?: 0,
        roomCount = input.roomCount ?: 0,
        isProofUploaded = input.isProofUploaded ?: false,
        hotelId = input.hotelId ?: "Empty",
        visitorId = input.visitorId?.id ?: "Empty",
        visitorName = "Empty",
        checkinDate = input.checkinDate ?: "Empty",
        checkoutDate = input.checkoutDate ?: "Empty",
        type = input.type ?: "Empty",
        id = input.id ?: "Empty",
        transactionProof = input.pathTransactionProof
            ?: "https://storage.googleapis.com/ace-hotel/codioful-formerly-gradienta-G084bO4wGDA-unsplash.jpg",
    )

    fun mapPayBookingResponseToDomain(input: PayBookingResponse): Booking = Booking(
        addOn = (input.addOnId ?: listOf()) as List<String>,
        totalPrice = input.totalPrice ?: 0,
        duration = input.duration ?: 0,
        roomCount = input.roomCount ?: 0,
        isProofUploaded = input.isProofUploaded ?: false,
        hotelId = input.hotelId ?: "Empty",
        visitorName = "Empty",
        visitorId = input.visitorId?.id ?: "Empty",
        checkinDate = input.checkinDate ?: "Empty",
        checkoutDate = input.checkoutDate ?: "Empty",
        type = input.type ?: "Empty",
        id = input.id ?: "Empty",
        transactionProof = input.pathTransactionProof
            ?: "https://storage.googleapis.com/ace-hotel/codioful-formerly-gradienta-G084bO4wGDA-unsplash.jpg",
        room = listOf(
            RoomBooking(
                id = input.roomId?.firstOrNull() ?: "Empty",
                actualCheckin = "Empty",
                actualCheckout = "Empty",
                checkoutStaffId = "Empty",
                checkinStaffId = "Empty",
                hasProblem = false,
                note = "Empty",
            )
        )

    )

    fun mapListBookingToDomain(input: ListBookingResponse): List<Booking> =
        input.results?.map { booking ->
            Timber.tag("TEST").e(booking?.room?.first()?.actualCheckin ?: "Empty")

            Booking(
                addOn = (booking?.addOnId ?: listOf()) as List<String>,
                room = booking?.room?.map {
                    RoomBooking(
                        id = it?.roomId ?: "Empty",
                        actualCheckin = it?.actualCheckin ?: "Empty",
                        actualCheckout = it?.actualCheckout ?: "Empty",
                        checkoutStaffId = it?.checkoutStaffId ?: "Empty",
                        checkinStaffId = it?.checkinStaffId ?: "Empty",
                        hasProblem = false,
                        note = "Empty",
                    )
                } ?: listOf(
                    RoomBooking(
                        id = "Empty",
                        actualCheckin = "Empty",
                        actualCheckout = "Empty",
                        checkoutStaffId = "Empty",
                        checkinStaffId = "Empty",
                        hasProblem = false,
                        note = "Empty",
                    )
                ),
                totalPrice = booking?.totalPrice ?: 0,
                duration = booking?.duration ?: 0,
                roomCount = booking?.roomCount ?: 0,
                isProofUploaded = booking?.isProofUploaded ?: false,
                hotelId = booking?.hotelId ?: "Empty",
                visitorId = booking?.visitorId?.id ?: "Empty",
                visitorName = booking?.visitorId?.name ?: "Empty",
                checkinDate = booking?.checkinDate ?: "Empty",
                checkoutDate = booking?.checkoutDate ?: "Empty",
                type = booking?.type ?: "Empty",
                id = booking?.id ?: "Empty",
                transactionProof = booking?.pathTransactionProof
                    ?: "https://storage.googleapis.com/ace-hotel/codioful-formerly-gradienta-G084bO4wGDA-unsplash.jpg",
            )
        } ?: listOf()

    fun mapListBookingResultItemToDomain(input: ListBookingResultsItem): Booking = Booking(
        addOn = (input.addOnId ?: listOf()) as List<String>,
        room = input.room?.map {
            RoomBooking(
                id = it?.roomId ?: "Empty",
                actualCheckin = it?.actualCheckin ?: "Empty",
                actualCheckout = it?.actualCheckout ?: "Empty",
                checkoutStaffId = it?.checkoutStaffId ?: "Empty",
                checkinStaffId = it?.checkinStaffId ?: "Empty",
                hasProblem = false,
                note = "Empty",
            )
        } ?: listOf(),
        totalPrice = input.totalPrice ?: 0,
        duration = input.duration ?: 0,
        roomCount = input.roomCount ?: 0,
        isProofUploaded = input.isProofUploaded ?: false,
        hotelId = input.hotelId ?: "Empty",
        visitorId = input.visitorId?.id ?: "Empty",
        visitorName = input.visitorId?.name ?: "Empty",
        checkinDate = input.checkinDate ?: "Empty",
        checkoutDate = input.checkoutDate ?: "Empty",
        type = input.type ?: "Empty",
        id = input.id ?: "Empty",
        transactionProof = input.pathTransactionProof
            ?: "https://storage.googleapis.com/ace-hotel/codioful-formerly-gradienta-G084bO4wGDA-unsplash.jpg",
    )
}