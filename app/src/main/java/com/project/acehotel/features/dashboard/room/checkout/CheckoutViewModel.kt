package com.project.acehotel.features.dashboard.room.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.room.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val roomUseCase: RoomUseCase
) : ViewModel() {
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
    ) = roomUseCase.roomCheckout(
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
    ).asLiveData()
}