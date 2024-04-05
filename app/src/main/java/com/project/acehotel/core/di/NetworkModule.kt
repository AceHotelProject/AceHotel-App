package com.project.acehotel.core.di

import android.content.Context
import com.project.acehotel.core.data.source.remote.MQTTService
import com.project.acehotel.core.data.source.remote.network.ApiService
import com.project.acehotel.core.di.interceptor.AuthAuthenticator
import com.project.acehotel.core.di.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.eclipse.paho.android.service.MqttAndroidClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class])
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMqttClient(@ApplicationContext context: Context): MqttAndroidClient {
        return MqttAndroidClient(context, server, clientId)
    }

    @Singleton
    @Provides
    fun provideMQTTService(client: MqttAndroidClient): MQTTService {
        return MQTTService(client)
    }

    companion object {
        const val BASE_URL = "https://acehotel.my.id/v1/"
        private const val server = "tcp://91.108.104.92:1883"
        private const val clientId = "test-app"
    }
}