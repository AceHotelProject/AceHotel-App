package com.project.acehotel.core.di.interceptor

import com.project.acehotel.core.data.source.local.datastore.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val tokenManager: TokenManager) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getAccessToken()
        }

        val request = chain.request().newBuilder()
        if (!token.toString().isNullOrEmpty()) {
            request.addHeader("Authorization", "Bearer $token")
        }
        
        return chain.proceed(request.build())
    }
}