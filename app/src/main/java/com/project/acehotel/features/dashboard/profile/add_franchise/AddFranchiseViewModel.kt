package com.project.acehotel.features.dashboard.profile.add_franchise

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.model.Hotel
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddFranchiseViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val hotelUseCase: HotelUseCase,
) : ViewModel() {

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()

    fun getHotel(id: String) = hotelUseCase.getHotel(id).asLiveData()

    private fun uploadImage(image: List<MultipartBody.Part>) =
        authUseCase.uploadImage(image).asLiveData()

    private fun addHotel(
        name: String,
        address: String,
        contact: String,

        regularRoomCount: Int,
        regularRoomImage: String,
        exclusiveRoomCount: Int,
        exclusiveRoomImage: String,
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

    fun executeAddHotel(
        image: List<MultipartBody.Part>,
        name: String,
        address: String,
        contact: String,

        regularRoomCount: Int,
        exclusiveRoomCount: Int,
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
    ): MediatorLiveData<Resource<ManageHotel>> = MediatorLiveData<Resource<ManageHotel>>().apply {
        addSource(uploadImage(image)) { result ->
            when (result) {
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Message -> {

                }
                is Resource.Success -> {
                    addSource(
                        addHotel(
                            name,
                            address,
                            contact,
                            regularRoomCount,
                            result.data?.get(0)
                                ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png",
                            exclusiveRoomCount,
                            result.data?.get(1)
                                ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png",
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
                        )
                    ) { hotel ->
                        value = hotel
                    }
                }
            }
        }
    }

    fun updateHotel(
        id: String,

        name: String,
        address: String,
        contact: String,

        regularRoomCount: Int,
        regularRoomImage: String,
        exclusiveRoomCount: Int,
        exclusiveRoomImage: String,
        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int,
    ) = hotelUseCase.updateHotel(
        id,
        name,
        address,
        contact,
        regularRoomCount,
        regularRoomImage,
        exclusiveRoomCount,
        exclusiveRoomImage,
        regularRoomPrice,
        exclusiveRoomPrice,
        extraBedPrice
    ).asLiveData()

    fun executeUpdateHotel(
        image: List<MultipartBody.Part>,

        id: String,

        name: String,
        address: String,
        contact: String,

        regularRoomCount: Int,
        regularRoomImage: String?,
        exclusiveRoomCount: Int,
        exclusiveRoomImage: String?,
        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int,

        isRegularImageChanged: Boolean,
        isExclusiveImageChanged: Boolean,
    ): MediatorLiveData<Resource<Hotel>> = MediatorLiveData<Resource<Hotel>>().apply {
        addSource(uploadImage(image)) { result ->
            when (result) {
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Message -> {

                }
                is Resource.Success -> {
                    Timber.tag("TEST").e(result.data.toString())

                    if (isRegularImageChanged && isExclusiveImageChanged) {
                        addSource(
                            updateHotel(
                                id,
                                name,
                                address,
                                contact,
                                regularRoomCount, result.data?.get(0)
                                    ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png",
                                exclusiveRoomCount,
                                result.data?.get(1)
                                    ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png",
                                regularRoomPrice,
                                exclusiveRoomPrice,
                                extraBedPrice
                            )
                        ) { hotel ->
                            value = hotel
                        }
                    } else if (isRegularImageChanged) {
                        addSource(
                            updateHotel(
                                id,
                                name,
                                address,
                                contact,
                                regularRoomCount, result.data?.get(0)
                                    ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png",
                                exclusiveRoomCount,
                                exclusiveRoomImage
                                    ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png",
                                regularRoomPrice,
                                exclusiveRoomPrice,
                                extraBedPrice
                            )
                        ) { hotel ->
                            value = hotel
                        }
                    } else {
                        addSource(
                            updateHotel(
                                id,
                                name,
                                address,
                                contact,
                                regularRoomCount,
                                regularRoomImage
                                    ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png",
                                exclusiveRoomCount,
                                result.data?.get(1)
                                    ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png",
                                regularRoomPrice,
                                exclusiveRoomPrice,
                                extraBedPrice
                            )
                        ) { hotel ->
                            value = hotel
                        }
                    }
                }
            }
        }
    }
}