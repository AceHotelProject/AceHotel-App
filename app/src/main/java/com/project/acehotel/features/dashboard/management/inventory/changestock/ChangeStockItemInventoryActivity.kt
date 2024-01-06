package com.project.acehotel.features.dashboard.management.inventory.changestock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityChangeStockItemInventoryBinding

class ChangeStockItemInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeStockItemInventoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangeStockItemInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}