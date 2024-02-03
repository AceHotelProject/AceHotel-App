package com.project.acehotel.core.di.interceptor

import com.project.acehotel.core.data.source.local.datastore.UserManager
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.network.ApiService
import com.project.acehotel.core.data.source.remote.response.auth.RefreshTokenResponse
import com.project.acehotel.core.di.NetworkModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val userManager: UserManager,
) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            userManager.getRefreshToken().first().toString()
        }

        Timber.tag("TOKEN").d("Ini refresh token %s", refreshToken)
        Timber.tag("TOKEN").d("Ini code %s", response.code)

        return if (response.code == 401 && refreshToken.isNotEmpty()) {
            runBlocking {
                when (val newRefreshToken = getNewRefreshToken(refreshToken)) {
                    ApiResponse.Empty -> {
                        userManager.deleteToken()

                        Timber.tag("AuthAuthenticator").e("Empty")
                        response.request.newBuilder().build()
                    }
                    is ApiResponse.Error -> {
                        userManager.deleteToken()

                        Timber.tag("AuthAuthenticator").e(newRefreshToken.errorMessage)
                        response.request.newBuilder().build()
                    }
                    is ApiResponse.Success -> {
                        userManager.saveAccessToken(newRefreshToken.data.access?.token.toString())
                        userManager.saveRefreshToken(newRefreshToken.data.refresh?.token.toString())

                        Timber.tag("TOKEN")
                            .d("Ini token baru %s", newRefreshToken.data.refresh?.token.toString())

                        response.request.newBuilder().header(
                            "Authorization",
                            "Bearer ${newRefreshToken.data.access?.token}"
                        ).build()
                    }
                }
            }
        } else {
            null
        }
    }

    private suspend fun getNewRefreshToken(refreshToken: String): ApiResponse<RefreshTokenResponse> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(NetworkModule.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(ApiService::class.java)

        return try {
            val response = service.refreshToken(refreshToken)

            if (response.access != null && response.refresh != null) {
                ApiResponse.Success(response)
            } else {
                ApiResponse.Empty
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.toString())
        }
    }
}