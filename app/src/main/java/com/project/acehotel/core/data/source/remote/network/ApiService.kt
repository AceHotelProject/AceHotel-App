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
    ): InventoryDetailResponse
    // INVENTORY
}