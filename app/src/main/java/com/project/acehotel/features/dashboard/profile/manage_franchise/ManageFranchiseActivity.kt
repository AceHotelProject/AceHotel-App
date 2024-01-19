package com.project.acehotel.features.dashboard.profile.manage_franchise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityManageFranchiseBinding

class ManageFranchiseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageFranchiseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManageFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}