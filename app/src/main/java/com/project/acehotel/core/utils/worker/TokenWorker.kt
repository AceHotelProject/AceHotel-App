package com.project.acehotel.core.utils.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.acehotel.R
import com.project.acehotel.core.data.repository.AuthRepository
import com.project.acehotel.core.data.repository.HotelRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.constants.NOTIFICATION_CHANNEL_ID
import com.project.acehotel.core.utils.constants.NOTIFICATION_ID
import com.project.acehotel.core.utils.isInternetAvailable
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@HiltWorker
class TokenWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,

    private val authUseCase: AuthRepository,
    private val hotelUseCase: HotelRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val selectedHotel = hotelUseCase.getSelectedHotelData().firstOrNull()
                ?: return@withContext Result.failure()

            hotelUseCase.getHotel(selectedHotel.id).collect { hotel ->
                when (hotel) {
                    is Resource.Error -> {
                        if (!isInternetAvailable(appContext)) {
                            //no internet
                        } else {
                            authUseCase.getRefreshToken().collect {
                                if (it.isEmpty()) {
                                    authUseCase.deleteToken()

                                    showNotification()
                                }
                            }
                        }
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Message -> {

                    }
                    is Resource.Success -> {

                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }

    }

    private fun showNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_ace_hotel)
            .setContentTitle("Sesi Anda Berakhir")
            .setContentText("Mohon lakukan login ulang dengan akun anda untuk dapat menggunakan aplikasi Ace Hotel")
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}