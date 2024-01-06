package com.project.acehotel.core.di

import com.project.acehotel.core.data.repository.AuthRepository
import com.project.acehotel.core.data.repository.InventoryRepository
import com.project.acehotel.core.domain.auth.repository.IAuthRepository
import com.project.acehotel.core.domain.inventory.repository.IInventoryIRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAuthRepository(authRepository: AuthRepository): IAuthRepository

    @Binds
    abstract fun provideInventoryRepository(inventoryIRepository: InventoryRepository): IInventoryIRepository

}