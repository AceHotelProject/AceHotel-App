package com.project.acehotel.features.dashboard.management.visitor

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.visitor.model.Visitor
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisitorViewModel @Inject constructor(
    private val visitorUseCase: VisitorUseCase,
    private val hotelUseCase: HotelUseCase,
    private val authUseCase: AuthUseCase,
) :
    ViewModel() {

    private fun getVisitorList(
        hotelId: String,
        name: String,
        email: String,
        identityNum: String
    ) = visitorUseCase.getVisitorList(hotelId, name, email, identityNum).asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun executeGetVisitorList(
        name: String,
        email: String,
        identityNum: String
    ): MediatorLiveData<Resource<List<Visitor>>> =
        MediatorLiveData<Resource<List<Visitor>>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(getVisitorList(hotel.id, name, email, identityNum)) { visitor ->
                    value = visitor
                }
            }
        }
}