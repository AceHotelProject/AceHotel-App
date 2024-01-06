package com.project.acehotel.core.data.source.remote.network

import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.data.source.remote.response.auth.RefreshTokenResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryDetailResponse
import com.project.acehotel.core.data.source.remote.response.inventory.InventoryListResponse
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
//        @Header("Authorization") token: String,
    ): InventoryListResponse

    @GET("inventory/{id}")
    suspend fun getDetailInventory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): InventoryDetailResponse

    // INVENTORY
}