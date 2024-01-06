package com.project.acehotel.features.dashboard.management.inventory.chooseitem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityChooseItemInventoryBinding

class ChooseItemInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseItemInventoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseItemInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}