package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.booking.AddBookingResponse
import com.project.acehotel.core.domain.booking.model.Booking

object BookingDataMapper {

    fun mapBookingResponseToDomain(input: AddBookingResponse): Booking = Booking(
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
    )
}