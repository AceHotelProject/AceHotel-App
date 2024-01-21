package com.project.acehotel.features.dashboard.profile.manage_franchise.add_franchise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityAddFranchiseBinding

class AddFranchiseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFranchiseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}