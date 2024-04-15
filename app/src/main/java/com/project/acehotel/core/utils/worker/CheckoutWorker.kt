package com.project.acehotel.core.utils.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.acehotel.R
import com.project.acehotel.core.data.repository.BookingRepository
import com.project.acehotel.core.data.repository.HotelRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.NOTIFICATION_CHANNEL_ID
import com.project.acehotel.core.utils.constants.NOTIFICATION_ID
import com.project.acehotel.features.dashboard.booking.choose_booking.ChooseBookingActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@HiltWorker
class CheckoutWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,

    private val bookingUseCase: BookingRepository,
    private val hotelUseCase: HotelRepository,
) : CoroutineWorker(appContext, workerParams) {

    private var countNotCheckout: Int = 0

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val selectedHotel = hotelUseCase.getSelectedHotelData().firstOrNull()
                ?: return@withContext Result.failure()
            val filterDate = DateUtils.getDateThisDay()

            bookingUseCase.getListBookingByHotel(
                selectedHotel.id,
                filterDate,
                "",
            ).collect { booking ->
                when (booking) {
                    is Resource.Error -> {
                        Result.failure()
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Message -> {
                        Result.failure()
                    }
                    is Resource.Success -> {
                        if (!booking.data.isNullOrEmpty()) {
                            for (item in booking.data) {
                                if ((DateUtils.isTodayDate(item.checkoutDate) ||
                                            DateUtils.isDateBeforeToday(item.checkoutDate)) &&
                                    item.room.first().actualCheckout == "Empty"
                                ) {
                                    ++countNotCheckout
                                }
                            }
                        }

                        if (countNotCheckout != 0) {
                            showNotification(countNotCheckout)
                        }
                    }
                }

            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun showNotification(numberNotCheckout: Int) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_ace_hotel)
            .setContentTitle("Booking Belum Checkout")
            .setContentIntent(getPendingIntent())
            .setContentText("Terdapat $numberNotCheckout booking yang belum checkout")
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, ChooseBookingActivity::class.java)
        intent.putExtra(FLAG_VISITOR, MENU_CHECKOUT)

        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
    }

    companion object {
        private const val MENU_CHECKOUT = "checkout"
        private const val FLAG_VISITOR = "flag_visitor"
    }
}