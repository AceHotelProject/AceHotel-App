package com.project.acehotel.core.di.interceptor

import com.project.acehotel.core.data.source.local.datastore.TokenManager
import com.project.acehotel.core.data.source.remote.network.ApiService
import com.project.acehotel.core.data.source.remote.response.auth.RefreshTokenResponse
import com.project.acehotel.core.di.NetworkModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            tokenManager.getRefreshToken().first().toString()
        }

        return runBlocking {
            val newRefreshToken = getNewRefreshToken(refreshToken)

            if (!newRefreshToken.refresh?.token.isNullOrEmpty() || !newRefreshToken.access?.token.isNullOrEmpty()) {
                tokenManager.deleteToken()
            }

            newRefreshToken.let {
                tokenManager.saveAccessToken(newRefreshToken.access?.token.toString())
                tokenManager.saveRefreshToken(newRefreshToken.refresh?.token.toString())

                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newRefreshToken.access?.token}").build()
            }
        }
    }

    private suspend fun getNewRefreshToken(refreshToken: String): RefreshTokenResponse {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(NetworkModule.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val service = retrofit.create(ApiService::class.java)
        return service.refreshToken(refreshToken)
    }
}