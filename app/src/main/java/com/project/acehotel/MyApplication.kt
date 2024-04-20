package com.project.acehotel

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.project.acehotel.core.utils.constants.NOTIFICATION_CHANNEL_ID
import com.project.acehotel.core.utils.constants.NOTIFICATION_DESC
import com.project.acehotel.core.utils.constants.NOTIFICATION_NAME
import com.project.acehotel.core.utils.worker.CheckoutWorker
import com.project.acehotel.core.utils.worker.TokenWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
open class MyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        setupNotificationChannel()

        startTokenWorker()

        startTokenWorker()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun startCheckoutWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorker =
            PeriodicWorkRequest.Builder(CheckoutWorker::class.java, 2, TimeUnit.DAYS)
                .setConstraints(constraints).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "checkoutWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorker
        )
    }

    private fun startTokenWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorker =
            PeriodicWorkRequest.Builder(TokenWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "tokenWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorker
        )
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NOTIFICATION_NAME
            val desc = NOTIFICATION_DESC
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = desc
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}