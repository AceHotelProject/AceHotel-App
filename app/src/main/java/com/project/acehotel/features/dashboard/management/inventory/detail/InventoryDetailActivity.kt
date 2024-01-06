package com.project.acehotel.features.dashboard.management.inventory.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityInventoryDetailBinding
import com.project.acehotel.features.dashboard.management.inventory.chooseitem.ChooseItemInventoryActivity

class InventoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInventoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleFab()
    }

    private fun handleFab() {
        binding.fabInventoryDetailChangeStock.setOnClickListener {
            val intentToChooseItem = Intent(this, ChooseItemInventoryActivity::class.java)
            startActivity(intentToChooseItem)
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}