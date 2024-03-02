package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.booking.AddBookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.BookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.ListBookingResponse
import com.project.acehotel.core.data.source.remote.response.booking.PayBookingResponse
import com.project.acehotel.core.domain.booking.model.Booking

object BookingDataMapper {

    fun mapAddBookingResponseToDomain(input: AddBookingResponse): Booking = Booking(
        roomId = (input.roomId ?: listOf()) as List<String>,
        addOn = (input.addOnId ?: listOf()) as List<String>,
        noteId = (input.noteId ?: listOf()) as List<String>,
        totalPrice = input.totalPrice ?: 0,
        duration = input.duration ?: 0,
        roomCount = input.roomCount ?: 0,
        hasProblem = input.hasProblem ?: false,
        isProofUploaded = input.isProofUploaded ?: false,
        hotelId = input.hotelId ?: "Empty",
        visitorId = input.visitorId ?: "Empty",
        checkinDate = input.checkinDate ?: "Empty",
        checkoutDate = input.checkoutDate ?: "Empty",
        type = input.type ?: "Empty",
        id = input.id ?: "Empty",
        transactionProof = "Empty",
        visitorName = "Empty"
    )

    fun mapBookingResponseToDomain(input: BookingResponse): Booking = Booking(
        roomId = (input.roomId ?: listOf()) as List<String>,
        addOn = (input.addOnId ?: listOf()) as List<String>,
        noteId = (input.noteId ?: listOf()) as List<String>,
        totalPrice = input.totalPrice ?: 0,
        duration = input.duration ?: 0,
        roomCount = input.roomCount ?: 0,
        hasProblem = input.hasProblem ?: false,
        isProofUploaded = input.isProofUploaded ?: false,
        hotelId = input.hotelId ?: "Empty",
        visitorId = input.visitorId ?: "Empty",
        visitorName = "Empty",
        checkinDate = input.checkinDate ?: "Empty",
        checkoutDate = input.checkoutDate ?: "Empty",
        type = input.type ?: "Empty",
        id = input.id ?: "Empty",
        transactionProof = input.pathTransactionProof
            ?: "https://storage.googleapis.com/ace-hotel/codioful-formerly-gradienta-G084bO4wGDA-unsplash.jpg",
    )

    fun mapPayBookingResponseToDomain(input: PayBookingResponse): Booking = Booking(
        roomId = (input.roomId ?: listOf()) as List<String>,
        addOn = (input.addOnId ?: listOf()) as List<String>,
        noteId = (input.noteId ?: listOf()) as List<String>,
        totalPrice = input.totalPrice ?: 0,
        duration = input.duration ?: 0,
        roomCount = input.roomCount ?: 0,
        hasProblem = input.hasProblem ?: false,
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
    )

    fun mapListBookingToDomain(input: ListBookingResponse): List<Booking> =
        input.results?.map { booking ->
            Booking(
                roomId = (booking?.roomId ?: listOf()) as List<String>,
                addOn = (booking?.addOnId ?: listOf()) as List<String>,
                noteId = (booking?.noteId ?: listOf()) as List<String>,
                totalPrice = booking?.totalPrice ?: 0,
                duration = booking?.duration ?: 0,
                roomCount = booking?.roomCount ?: 0,
                hasProblem = booking?.hasProblem ?: false,
                isProofUploaded = booking?.isProofUploaded ?: false,
                hotelId = booking?.hotelId ?: "Empty",
                visitorId = booking?.visitorId?.id ?: "Empty",
                checkinDate = booking?.checkinDate ?: "Empty",
                checkoutDate = booking?.checkoutDate ?: "Empty",
                type = booking?.type ?: "Empty",
                id = booking?.id ?: "Empty",
                visitorName = booking?.visitorId?.name ?: "Empty",
                transactionProof = booking?.pathTransactionProof
                    ?: "https://storage.googleapis.com/ace-hotel/codioful-formerly-gradienta-G084bO4wGDA-unsplash.jpg",
            )
        } ?: listOf()
}