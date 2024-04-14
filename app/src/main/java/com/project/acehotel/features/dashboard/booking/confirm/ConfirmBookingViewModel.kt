package com.project.acehotel.features.dashboard.booking.confirm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ConfirmBookingViewModel @Inject constructor(
    private val hotelUseCase: HotelUseCase,
    private val bookingUseCase: BookingUseCase,
    private val visitorUseCase: VisitorUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    private fun addBooking(
        hotelId: String,
        visitorId: String,
        checkinDate: String,
        duration: Int,
        roomCount: Int,
        extraBed: Int,
        type: String,
    ) = bookingUseCase.addBooking(
        hotelId,
        visitorId,
        checkinDate,
        duration,
        roomCount,
        extraBed,
        type
    ).asLiveData()

    private fun uploadImage(image: List<MultipartBody.Part>) =
        authUseCase.uploadImage(image).asLiveData()

    fun executeAddBooking(
        visitorId: String,
        checkinDate: String,
        duration: Int,
        roomCount: Int,
        extraBed: Int,
        type: String,

        discountCode: String,
        transactionProof: List<MultipartBody.Part>
    ): MediatorLiveData<Resource<Booking>> = MediatorLiveData<Resource<Booking>>().apply {
        addSource(getSelectedHotelData()) { hotel ->
            addSource(
                addBooking(
                    hotel.id,
                    visitorId,
                    checkinDate,
                    duration,
                    roomCount,
                    extraBed,
                    type
                )
            ) { booking ->
                when (booking) {
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Message -> {

                    }
                    is Resource.Success -> {
                        addSource(uploadImage(transactionProof)) { transactionProof ->
                            when (transactionProof) {
                                is Resource.Error -> {

                                }
                                is Resource.Loading -> {

                                }
                                is Resource.Message -> {

                                }
                                is Resource.Success -> {
                                    addSource(
                                        payBooking(
                                            booking.data?.id!!,
                                            transactionProof.data?.get(0)
                                                ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png",
                                        )
                                    ) { payBooking ->
                                        if (discountCode.isNotEmpty() && discountCode != "Tidak Menggunakan Diskon") {
                                            addSource(
                                                applyDiscount(
                                                    booking.data.id,
                                                    discountCode
                                                )
                                            ) { discount ->
                                                value = discount
                                            }
                                        } else {
                                            value = payBooking
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getVisitorDetail(id: String) = visitorUseCase.getVisitorDetail(id).asLiveData()

    private fun payBooking(
        id: String,
        transactionProof: String
    ) = bookingUseCase.payBooking(id, transactionProof).asLiveData()

    private fun applyDiscount(
        id: String,
        discountCode: String
    ) = bookingUseCase.applyDiscount(id, discountCode).asLiveData()

    fun getRefreshToken() = authUseCase.getRefreshToken().asLiveData()
}