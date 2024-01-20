package com.project.acehotel.features.dashboard.profile.stats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityOverallStatsBinding

class OverallStatsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverallStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOverallStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}