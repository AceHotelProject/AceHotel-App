package com.project.acehotel.core.di

import com.project.acehotel.core.data.repository.*
import com.project.acehotel.core.domain.auth.repository.IAuthRepository
import com.project.acehotel.core.domain.booking.repository.IBookingRepository
import com.project.acehotel.core.domain.hotel.repository.IHotelRepository
import com.project.acehotel.core.domain.inventory.repository.IInventoryIRepository
import com.project.acehotel.core.domain.visitor.repository.IVisitorRepository
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

    @Binds
    abstract fun provideHotelRepository(hotelRepository: HotelRepository): IHotelRepository

    @Binds
    abstract fun provideVisitorRepository(visitorRepository: VisitorRepository): IVisitorRepository

    @Binds
    abstract fun provideBookingRepository(bookingRepository: BookingRepository): IBookingRepository
}