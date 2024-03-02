package com.project.acehotel.features.dashboard.room.change_price

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.Hotel
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePriceViewModel @Inject constructor(
    private val hotelUseCase: HotelUseCase
) : ViewModel() {

    private fun updateHotelPrice(
        id: String,
        discountCode: String,
        discountAmount: Int,
        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int,
    ) = hotelUseCase.updateHotelPrice(
        id,
        discountCode,
        discountAmount,
        regularRoomPrice,
        exclusiveRoomPrice,
        extraBedPrice
    ).asLiveData()

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun saveSelectedHotelData(data: ManageHotel) = viewModelScope.launch {
        hotelUseCase.saveSelectedHotelData(data)
    }

    fun executeUpdateHotelPrice(
        discountCode: String,
        discountAmount: Int,

        regularRoomPrice: Int,
        exclusiveRoomPrice: Int,
        extraBedPrice: Int,
    ): MediatorLiveData<Resource<Hotel>> = MediatorLiveData<Resource<Hotel>>().apply {
        addSource(getSelectedHotelData()) { hotel ->
            addSource(
                updateHotelPrice(
                    hotel.id,
                    discountCode,
                    discountAmount,
                    regularRoomPrice,
                    exclusiveRoomPrice,
                    extraBedPrice
                )
            ) { hotelPrice ->
                value = hotelPrice
            }
        }
    }
}