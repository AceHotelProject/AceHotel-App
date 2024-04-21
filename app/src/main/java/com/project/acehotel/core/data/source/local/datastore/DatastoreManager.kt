package com.project.acehotel.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    fun saveAccessToken(token: String): Flow<Boolean> {
        return flow {
            try {
                dataStore.edit { preferences ->
                    preferences[ACCESS_TOKEN_KEY] = token
                }
                emit(true)
            } catch (e: Exception) {
                emit(false)
                Timber.tag("UserManager").e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun saveRefreshToken(token: String): Flow<Boolean> {
        return flow {
            try {
                dataStore.edit { preferences ->
                    preferences[REFRESH_TOKEN_KEY] = token
                }
                emit(true)
            } catch (e: Exception) {
                emit(false)
                Timber.tag("UserManager").e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getAccessToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY] ?: ""
        }
    }

    fun getRefreshToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY] ?: ""
        }
    }

    suspend fun deleteToken() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    fun saveCurrentHotelData(data: ManageHotel): Flow<Boolean> {
        return flow {
            try {
                dataStore.edit { preferences ->
                    val jsonData = Gson().toJson(data)

                    preferences[CURRENT_HOTEL_DATA] = jsonData
                }
                emit(true)
            } catch (e: Exception) {
                emit(false)
                Timber.tag("UserManager").e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getCurrentHotelData(): Flow<ManageHotel> {
        return dataStore.data.map { preferences ->
            if (!preferences[CURRENT_HOTEL_DATA].isNullOrEmpty()) {
                Gson().fromJson(preferences[CURRENT_HOTEL_DATA], ManageHotel::class.java)
            } else {
                ManageHotel()
            }
        }
    }


    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")

        private val CURRENT_HOTEL_DATA = stringPreferencesKey("current_hotel_data")
    }
}