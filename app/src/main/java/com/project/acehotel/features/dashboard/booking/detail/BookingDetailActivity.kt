package com.project.acehotel.features.dashboard.booking.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityBookingDetailBinding

class BookingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}