package com.project.acehotel.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.project.acehotel.core.data.source.local.datastore.TokenManager
import com.project.acehotel.core.data.source.local.room.UserDao
import com.project.acehotel.core.data.source.local.room.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase = Room.databaseBuilder(
        context,
        UserDatabase::class.java, "User.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideUserDao(database: UserDatabase): UserDao = database.userDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
            migrations = listOf(SharedPreferencesMigration(context, TOKEN_MANAGER)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(TOKEN_MANAGER) })
    }

    @Provides
    fun provideTokenManager(dataStore: DataStore<Preferences>): TokenManager =
        TokenManager(dataStore)

    companion object {
        const val TOKEN_MANAGER = "token_manager"
    }
}