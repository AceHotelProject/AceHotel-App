package com.project.acehotel.features.dashboard.profile.update_user

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityUpdateUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserBinding

    private val updateUserViewModel: UpdateUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        handleButtonBack()

        enableRefresh(false)

        validateToken()
    }

    private fun validateToken() {
        updateUserViewModel
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refUpdateInventory.isRefreshing = isLoading
    }

    private fun enableRefresh(isEnable: Boolean) {
        binding.refUpdateInventory.isEnabled = isEnable
    }
}