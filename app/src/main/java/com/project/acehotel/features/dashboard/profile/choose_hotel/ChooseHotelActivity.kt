package com.project.acehotel.features.dashboard.profile.choose_hotel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityChooseHotelBinding

class ChooseHotelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseHotelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}