package com.project.acehotel.core.data.source.remote.network

import com.project.acehotel.core.data.source.remote.response.ListTourismResponse
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("list")
    suspend fun getList(): ListTourismResponse


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

    // AUTH

}