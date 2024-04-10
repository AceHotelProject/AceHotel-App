package com.project.acehotel.core.data.source.remote.response.note

import com.google.gson.annotations.SerializedName

data class NoteResponse(

    @field:SerializedName("booking_id")
    val bookingId: List<String?>? = null,

    @field:SerializedName("detail")
    val detail: String? = null,

    @field:SerializedName("id")
    val id: String? = null
)
