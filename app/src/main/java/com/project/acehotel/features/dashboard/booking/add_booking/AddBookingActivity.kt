package com.project.acehotel.features.dashboard.booking.add_booking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityAddBookingBinding

class AddBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBookingBinding.inflate(layoutInflater)

        setupActionBar()

        handleButtonBack()

        isButtonEnabled(false)

        disableRefresh()

        handleEditText()
    }

    private fun handleEditText() {
        TODO("Not yet implemented")
    }

    private fun checkForms() {
        TODO("Not yet implemented")
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnSave.isEnabled = isEnabled
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refAddBooking.isRefreshing = isLoading
    }

    private fun disableRefresh() {
        binding.refAddBooking.isEnabled = false
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}