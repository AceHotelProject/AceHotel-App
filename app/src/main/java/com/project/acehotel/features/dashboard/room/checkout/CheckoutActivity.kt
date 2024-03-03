package com.project.acehotel.features.dashboard.room.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refCheckout.isRefreshing = isLoading
    }

    private fun disableRefresh() {
        binding.refCheckout.isEnabled = false
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}