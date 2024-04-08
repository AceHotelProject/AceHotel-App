package com.project.acehotel.core.domain.room.interactor

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.room.model.Room
import com.project.acehotel.core.domain.room.repository.IRoomRepository
import com.project.acehotel.core.domain.room.usecase.RoomUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomInteractor @Inject constructor(private val roomRepository: IRoomRepository) :
    RoomUseCase {
    override fun getListRoomByHotel(hotelId: String): Flow<Resource<List<Room>>> {
        return roomRepository.getListRoomByHotel(hotelId)
    }

    override fun getRoomDetail(roomId: String): Flow<Resource<Room>> {
        return roomRepository.getRoomDetail(roomId)
    }

    override fun roomCheckin(
        roomId: String,
        checkinDate: String,
        bookingId: String,
        visitorId: String
    ): Flow<Resource<Room>> {
        return roomRepository.roomCheckin(roomId, checkinDate, bookingId, visitorId)
    }

    override fun roomCheckout(
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
        note: String
    ): Flow<Resource<Room>> {
        return roomRepository.roomCheckout(
            roomId,
            checkoutDate,
            bookingId,
            visitorId,
            facilityBantalPutih,
            facilityBantalHitam,
            facilityTv,
            facilityRemoteTv,
            facilityRemoteAc,
            facilityGantunganVaju,
            facilityKarpet,
            facilityCerminWastafel,
            facilityShower,
            facilitySelendang,
            facilityKerangjangSampah,
            facilityKursi,
            note
        )
    }
}