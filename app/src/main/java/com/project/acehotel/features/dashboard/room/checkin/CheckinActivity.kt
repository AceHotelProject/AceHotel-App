package com.project.acehotel.features.dashboard.room.checkin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityCheckinBinding

class CheckinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckinBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}