package com.project.acehotel.features.dashboard.profile.add_franchise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AddFranchiseViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val hotelUseCase: HotelUseCase,
) : ViewModel() {

    fun uploadImage(image: MultipartBody.Part) = authUseCase.uploadImage(image).asLiveData()

    fun addHotel(
        name: String,
        address: String,
        contact: String,

        regularRoomCount: Int,
        regularRoomImage: MultipartBody.Part,
        exclusiveRoomCount: Int,
        exclusiveRoomImage: MultipartBody.Part,
        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int,

        ownerName: String,
        ownerEmail: String,
        ownerPassword: String,

        receptionistName: String,
        receptionistEmail: String,
        receptionistPassword: String,

        cleaningName: String,
        cleaningEmail: String,
        cleaningPassword: String,

        inventoryName: String,
        inventoryEmail: String,
        inventoryPassword: String,
    ) = hotelUseCase.addHotel(
        name,
        address,
        contact,
        regularRoomCount,
        regularRoomImage,
        exclusiveRoomCount,
        exclusiveRoomImage,
        regularRoomPrice,
        exclusiveRoomPrice,
        extraBedPrice,
        ownerName,
        ownerEmail,
        ownerPassword,
        receptionistName,
        receptionistEmail,
        receptionistPassword,
        cleaningName,
        cleaningEmail,
        cleaningPassword,
        inventoryName,
        inventoryEmail,
        inventoryPassword
    ).asLiveData()


}