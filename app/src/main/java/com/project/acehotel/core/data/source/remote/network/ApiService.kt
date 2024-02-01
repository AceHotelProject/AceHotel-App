package com.project.acehotel.core.data.source.remote.network

import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.data.source.remote.response.auth.RefreshTokenResponse
import com.project.acehotel.core.data.source.remote.response.hotel.HotelResponse
import com.project.acehotel.core.data.source.remote.response.hotel.ListHotelResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    // AUTH
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("auth/refresh-tokens")
    suspend fun refreshToken(
        @Field("refreshToken") refreshToken: String
    ): RefreshTokenResponse
    // AUTH

    // INVENTORY

    @GET("inventory")
    suspend fun getListInventory(
    ): InventoryListResponse

    @GET("inventory/{id}")
    suspend fun getDetailInventory(
        @Path("id") id: String
    ): InventoryDetailResponse

    @POST("inventory")
    @FormUrlEncoded
    suspend fun addInventory(
        @Field("name") name: String,
        @Field("type") type: String,
        @Field("stock") stock: Int,
    ): InventoryDetailResponse

    @PATCH("inventory/{id}")
    @FormUrlEncoded
    suspend fun updateInventory(
        @Path("id") id: String,
        @Field("name") name: String,
        @Field("type") type: String,
        @Field("stock") stock: Int,
        @Field("title") title: String,
        @Field("description") description: String,
    ): InventoryDetailResponse

    @DELETE("inventory/{id}")
    suspend fun deleteInventory(
        @Path("id") id: String,
    ): Response<InventoryDetailResponse>
    // INVENTORY

    // HOTELS
    @Multipart
    @POST("hotels")
    suspend fun addHotel(
        @Part("name") name: String,
        @Part("address") address: String,
        @Part("contact") contact: String,

        @Part("regular_room_count") regularRoomCount: Int,
        @Part regularRoomImage: MultipartBody.Part,
        @Part("exclusive_room_count") exclusiveRoomCount: Int,
        @Part("exclusive_room_image") exclusiveRoomImage: MultipartBody.Part,
        @Part("regular_room_price") regularRoomPrice: Int,
        @Part("exclusive_room_price") exclusiveRoomPrice: Int,
        @Part("extra_bed_price") extraBedPrice: Int,

        @Part("owner_name") ownerName: String,
        @Part("owner_email") ownerEmail: String,
        @Part("owner_password") ownerPassword: String,

        @Part("receptionist_name") receptionistName: String,
        @Part("receptionist_email") receptionistEmail: String,
        @Part("receptionist_password") receptionistPassword: String,

        @Part("cleaning_name") cleaningName: String,
        @Part("cleaning_email") cleaningEmail: String,
        @Part("cleaning_password") cleaningPassword: String,

        @Part("inventory_name") inventoryName: String,
        @Part("inventory_email") inventoryEmail: String,
        @Part("inventory_password") inventoryPassword: String,
    ): HotelResponse

    @GET("hotels")
    suspend fun getHotels(): ListHotelResponse

    @GET("hotels/{id}")
    suspend fun getHotel(
        @Path("id") id: String
    ): HotelResponse

    @FormUrlEncoded
    @PATCH("hotels/{id}")
    suspend fun updateHotel(
        @Path("id") id: String,

        @Field("name") name: String,
        @Field("address") address: String,
        @Field("contact") contact: String,

        @Field("regular_room_count") regularRoomCount: Int,
        @Field("regular_room_image") regularRoomImage: MultipartBody.Part,
        @Field("exclusive_room_count") exclusiveRoomCount: Int,
        @Field("exclusive_room_image") exclusiveRoomImage: MultipartBody.Part,
        @Field("regular_room_price") regularRoomPrice: Int,
        @Field("exclusive_room_price") exclusiveRoomPrice: Int,
        @Field("extra_bed_price") extraBedPrice: Int,

        @Field("owner_name") ownerName: String,
        @Field("owner_email") ownerEmail: String,
        @Field("owner_password") ownerPassword: String,

        @Field("receptionist_name") receptionistName: String,
        @Field("receptionist_email") receptionistEmail: String,
        @Field("receptionist_password") receptionistPassword: String,

        @Field("cleaning_name") cleaningName: String,
        @Field("cleaning_email") cleaningEmail: String,
        @Field("cleaning_password") cleaningPassword: String,

        @Field("inventory_name") inventoryName: String,
        @Field("inventory_email") inventoryEmail: String,
        @Field("inventory_password") inventoryPassword: String,
    ): HotelResponse

    @DELETE("hotels/{id}")
    suspend fun deleteHotel(
        @Path("id") id: String
    ): Response<HotelResponse>
    // HOTELS
}