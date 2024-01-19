package com.project.acehotel.features.dashboard.management.visitor.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityVisitorDetailBinding

class VisitorDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVisitorDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVisitorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}