package com.project.acehotel.di

import com.project.acehotel.core.domain.auth.interactor.AuthInteractor
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.booking.interactor.BookingInteractor
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.interactor.HotelInteractor
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.inventory.interactor.InventoryInteractor
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import com.project.acehotel.core.domain.visitor.interactor.VisitorInteractor
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideInventoryUseCase(inventoryInteractor: InventoryInteractor): InventoryUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHotelUseCase(hotelInteractor: HotelInteractor): HotelUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideVisitorUseCase(visitorInteractor: VisitorInteractor): VisitorUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideBookingUseCase(bookingInteractor: BookingInteractor): BookingUseCase
}