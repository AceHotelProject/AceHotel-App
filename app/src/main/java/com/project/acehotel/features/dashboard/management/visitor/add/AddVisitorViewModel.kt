package com.project.acehotel.features.dashboard.management.visitor.add

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.visitor.model.Visitor
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AddVisitorViewModel @Inject constructor(
    private val visitorUseCase: VisitorUseCase,
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private fun addVisitor(
        hotelId: String,
        name: String,
        address: String,
        phone: String,
        email: String,
        identityNum: String,
        pathIdentityImage: String,
    ) = visitorUseCase.addVisitor(
        hotelId,
        name,
        address,
        phone,
        email,
        identityNum,
        pathIdentityImage
    ).asLiveData()

    private fun uploadImage(image: List<MultipartBody.Part>) =
        authUseCase.uploadImage(image).asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun executeAddVisitor(
        image: List<MultipartBody.Part>,

        name: String,
        address: String,
        phone: String,
        email: String,
        identityNum: String,
    ): MediatorLiveData<Resource<Visitor>> = MediatorLiveData<Resource<Visitor>>().apply {
        addSource(getSelectedHotelData()) { hotel ->
            addSource(uploadImage(image)) { image ->
                when (image) {
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Message -> {

                    }
                    is Resource.Success -> {
                        addSource(
                            addVisitor(
                                hotel.id,
                                name,
                                address,
                                phone,
                                email,
                                identityNum,
                                image.data?.firstOrNull()
                                    ?: "https://storage.googleapis.com/ace-hotel/placeholder_image.png"
                            )
                        ) { visitor ->
                            value = visitor
                        }
                    }
                }
            }
        }
    }

    fun getVisitorDetail(id: String) = visitorUseCase.getVisitorDetail(id).asLiveData()
}