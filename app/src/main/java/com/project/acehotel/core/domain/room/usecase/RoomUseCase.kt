package com.project.acehotel.core.domain.room.usecase

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.room.model.Room
import kotlinx.coroutines.flow.Flow

interface RoomUseCase {

    fun getListRoomByHotel(hotelId: String): Flow<Resource<List<Room>>>

    fun getRoomDetail(roomId: String): Flow<Resource<Room>>

    fun roomCheckin(
        roomId: String,
        checkinDate: String,
        bookingId: String,
        visitorId: String
    ): Flow<Resource<Room>>

    fun roomCheckout(
        roomId: String,
        checkoutDate: String,
        bookingId: String,
        visitorId: String,

        facilityBantalPutih: Boolean,
        facilityBantalHitam: Boolean,
        facilityTv: Boolean,
        facilityRemoteTv: Boolean,
        facilityRemoteAc: Boolean,
        facilityGantunganVaju: Boolean,
        facilityKarpet: Boolean,
        facilityCerminWastafel: Boolean,
        facilityShower: Boolean,
        facilitySelendang: Boolean,
        facilityKerangjangSampah: Boolean,
        facilityKursi: Boolean,

        note: String,
    ): Flow<Resource<Room>>
}