package com.project.acehotel.features.dashboard.management.inventory.edit_reader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityEditReaderBinding

class EditReaderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditReaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refEditReader.isRefreshing = isLoading
    }
}