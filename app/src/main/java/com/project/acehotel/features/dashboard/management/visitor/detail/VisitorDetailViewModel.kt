package com.project.acehotel.features.dashboard.management.visitor.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisitorDetailViewModel @Inject constructor(
    private val visitorUseCase: VisitorUseCase,
    private val hotelUseCase: HotelUseCase,
) : ViewModel() {
    fun getVisitorDetail(id: String) = visitorUseCase.getVisitorDetail(id).asLiveData()

//    private fun getSelectedHotel() = hotelUseCase.getSelectedHotel().asLiveData()
//
//    fun executeGetVisitorDetail(): MediatorLiveData<Resource<Visitor>> =
//        MediatorLiveData<Resource<Visitor>>().apply {
//            addSource(getSelectedHotel()) { hotel ->
//                addSource(getVisitorDetail(hotel)) { visitor ->
//                    value = visitor
//                }
//            }
//        }
}