package com.project.acehotel.features.popup.delete

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.booking.usecase.BookingUseCase
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.domain.hotel.usecase.HotelUseCase
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import com.project.acehotel.core.domain.room.usecase.RoomUseCase
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteItemViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val hotelUseCase: HotelUseCase,
    private val visitorUseCase: VisitorUseCase,
    private val bookingUseCase: BookingUseCase,
    private val roomUseCase: RoomUseCase,
    private val authUseCase: AuthUseCase,
) :
    ViewModel() {

    fun getSelectedHotelData() = hotelUseCase.getSelectedHotelData().asLiveData()

    fun saveSelectedHotelData(data: ManageHotel) =
        hotelUseCase.saveSelectedHotelData(data).asLiveData()

    private fun deleteInventory(id: String, hotelId: String) =
        inventoryUseCase.deleteInventory(id, hotelId).asLiveData()

    fun executeDeleteInventory(id: String): MediatorLiveData<Resource<Int>> =
        MediatorLiveData<Resource<Int>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(deleteInventory(id, hotel.id)) { hotel ->
                    value = hotel
                }
            }
        }

    fun executeDeleteHotel(id: String) = hotelUseCase.deleteHotel(id).asLiveData()

    private fun deleteVisitor(id: String, hotelId: String) =
        visitorUseCase.deleteVisitor(id, hotelId).asLiveData()

    fun executeDeleteVisitor(id: String): MediatorLiveData<Resource<Int>> =
        MediatorLiveData<Resource<Int>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(deleteVisitor(id, hotel.id)) { visitor ->
                    value = visitor
                }
            }
        }

    fun executeDeleteBooking(id: String) = bookingUseCase.deleteBooking(id).asLiveData()

    fun executeDeleteRoom(id: String) = roomUseCase.deleteRoom(id).asLiveData()

    private fun deleteUserAccount(id: String, hotelId: String) =
        authUseCase.deleteUserAccount(id, hotelId).asLiveData()

    fun executeDeleteUserAccount(id: String): MediatorLiveData<Resource<Int>> =
        MediatorLiveData<Resource<Int>>().apply {
            addSource(getSelectedHotelData()) { hotel ->
                addSource(deleteUserAccount(id, hotel.id)) { user ->
                    value = user
                }
            }
        }
}