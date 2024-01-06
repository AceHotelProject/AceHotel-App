package com.project.acehotel.features.dashboard.management.inventory.additem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityAddItemInventoryBinding

class AddItemInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddItemInventoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddItemInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}