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
        val outputFormat = SimpleDateFormat("d MMM yy", Locale.getDefault())

        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Set the UTC time zone for parsing

        return try {
            val date: Date = inputFormat.parse(inputDateString) ?: return ""
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun convertToDisplayDateFormat2(dateStr: String): String {
        // Define the input and output date formats
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val desiredFormat = SimpleDateFormat("d-M-yyyy", Locale.getDefault())

        // Parse the input date string into a Date object
        val date = sourceFormat.parse(dateStr)

        // Format the Date object into the desired format and return it
        return desiredFormat.format(date)
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
        val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")

        val checkinDate = LocalDate.parse(date, formatter)
        val checkoutDate = checkinDate.plusDays(duration)

        return checkoutDate.format(formatter)
    }

    fun convertToDisplayDateFormat(dateStr: String): String {
        // Define the input and output date formats
        val sourceFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val desiredFormat = SimpleDateFormat("d-M-yyyy", Locale.getDefault())

        // Parse the input date string into a Date object
        val date = sourceFormat.parse(dateStr)

        // Format the Date object into the desired format and return it
        return desiredFormat.format(date)
    }

    fun getDateThisDay(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())

        return dateFormat.format(calendar.time)
    }

    fun getDateThisMonth(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-M", Locale.getDefault())

        return dateFormat.format(calendar.time)
    }

    fun getDateThisYear(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        return dateFormat.format(calendar.time)
    }

    fun isTodayDate(checkinDateStr: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val checkinDate: Date = dateFormat.parse(checkinDateStr) ?: return false

        val checkinCalendar = Calendar.getInstance()
        checkinCalendar.time = checkinDate

        val todayCalendar = Calendar.getInstance()

        return (checkinCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                checkinCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                checkinCalendar.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH))
    }
}