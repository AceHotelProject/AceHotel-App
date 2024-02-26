package com.project.acehotel.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {
    fun convertDate(inputDateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yy", Locale.getDefault())

        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Set the UTC time zone for parsing

        return try {
            val date: Date = inputFormat.parse(inputDateString) ?: return ""
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getCompleteCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val completeDateFormat = SimpleDateFormat("dd-MMM-yyyy-HH:mm:ss", Locale.getDefault())
        return completeDateFormat.format(calendar.time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateCheckoutDate(date: String, duration: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/M/dd")

        val checkinDate = LocalDate.parse(date, formatter)
        val checkoutDate = checkinDate.plusDays(duration)

        return checkoutDate.format(formatter)
    }
}