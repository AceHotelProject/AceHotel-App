package com.project.acehotel.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
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

    suspend fun saveCurrentHotelId(id: String) {
        dataStore.edit { preferences ->
            preferences[CURRENT_HOTEL_ID] = id
        }
    }

    fun getCurrentHotelId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[CURRENT_HOTEL_ID] ?: ""
        }
    }

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")

        private val CURRENT_HOTEL_ID = stringPreferencesKey("current_hotel_id")
    }
}