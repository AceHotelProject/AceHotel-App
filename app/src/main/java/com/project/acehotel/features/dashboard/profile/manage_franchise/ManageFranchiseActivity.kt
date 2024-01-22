package com.project.acehotel.features.dashboard.profile.manage_franchise

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityManageFranchiseBinding
import com.project.acehotel.features.dashboard.profile.add_franchise.AddFranchiseActivity

class ManageFranchiseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageFranchiseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManageFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleButtonAdd()

        setupActionBar()

        handleButtonBack()
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun handleButtonAdd() {
        binding.btnAddFranchiseHotel.setOnClickListener {
            val intentToAddFranchise = Intent(this, AddFranchiseActivity::class.java)
            startActivity(intentToAddFranchise)
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}