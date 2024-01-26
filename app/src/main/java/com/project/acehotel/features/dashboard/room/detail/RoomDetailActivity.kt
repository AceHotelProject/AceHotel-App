package com.project.acehotel.features.dashboard.room.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityRoomDetailBinding

class RoomDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoomDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}