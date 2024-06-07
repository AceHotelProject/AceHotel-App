package com.project.acehotel.core.data.source.remote.response.tag

import com.google.gson.annotations.SerializedName

data class ReaderResponse(

    @field:SerializedName("read_interval")
    val readInterval: Int? = null,

    @field:SerializedName("reader_name")
    val readerName: String? = null,

    @field:SerializedName("tag_expired")
    val tagExpired: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("id")
    val id: String? = null
)
