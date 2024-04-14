package com.project.acehotel.features.dashboard.room.checkin

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.CurrentVisitorStatus
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityCheckinBinding
import com.project.acehotel.features.dashboard.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CheckinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckinBinding

    private val checkinViewModel: CheckinViewModel by viewModels()

    private var bookingData: Booking? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        isButtonEnabled(false)
        isEditTextEditable(binding.edCheckinRoom, false)
        isEditTextEditable(binding.edCheckinTime, false)

        enableRefresh(false)

        fetchBookingInfo()

        fetchRoomInfo()

        handleButtonSave()
    }

    private fun handleButtonSave() {
        binding.btnCheckin.setOnClickListener {
            isButtonEnabled(false)
            enableRefresh(true)

            checkinViewModel.roomCheckin(
                bookingData!!.room.first().id,
                DateUtils.getDateThisDay(),
                bookingData!!.id,
                bookingData!!.visitorId
            ).observe(this) { room ->
                when (room) {
                    is Resource.Error -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        if (!isInternetAvailable(this@CheckinActivity)) {
                            showToast(getString(R.string.check_internet))
                        } else {
                            showToast(room.message.toString())
                        }
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                        isButtonEnabled(false)
                    }
                    is Resource.Message -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        Timber.tag("CheckinActivity").d(room.message)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        showToast("Pengunjung telah berhasil checkin")

                        val intentToMain = Intent(this@CheckinActivity, MainActivity::class.java)
                        startActivity(intentToMain)
                        finish()
                    }
                }
            }
        }
    }

    private fun fetchRoomInfo() {
        checkinViewModel.getRoomDetail(bookingData?.room?.first()?.id!!).observe(this) { room ->
            when (room) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this@CheckinActivity)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        showToast(room.message.toString())
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)

                    Timber.tag("CheckinActivity").d(room.message)
                }
                is Resource.Success -> {
                    showLoading(false)

                    binding.apply {
                        edCheckinRoom.setText(room.data?.name)
                        edCheckinTime.setText(DateUtils.getCurrentDateTime())
                    }

                    isButtonEnabled(true)
                }
            }
        }
    }

    private fun fetchBookingInfo() {
        val data = intent.getStringExtra(BOOKING_DATA)
        bookingData = Gson().fromJson(data, Booking::class.java)

        if (bookingData != null) {
            binding.apply {
                customCardVisitor.setVisitorDate(
                    "${DateUtils.convertDate(bookingData!!.checkinDate)} - ${
                        DateUtils.convertDate(
                            bookingData!!.checkoutDate
                        )
                    }"
                )
                customCardVisitor.setVisitorName(bookingData!!.visitorName)
                customCardVisitor.setVisitorStatus(CurrentVisitorStatus.CHECKIN.status, "", false)
            }
        }
    }

    private fun isEditTextEditable(editText: TextInputEditText, isEditable: Boolean) {
        editText.isFocusable = isEditable
        editText.isClickable = isEditable
        editText.isFocusableInTouchMode = isEditable
        editText.isCursorVisible = isEditable
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refCheckin.isRefreshing = isLoading
    }

    private fun enableRefresh(isEnabled: Boolean) {
        binding.refCheckin.isEnabled = isEnabled
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnCheckin.isEnabled = isEnabled
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
    }
}