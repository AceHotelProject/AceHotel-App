package com.project.acehotel.features.dashboard.profile.manage_franchise

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.Hotel
import com.project.acehotel.core.ui.adapter.hotel.ManageHotelListAdapter
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityManageFranchiseBinding
import com.project.acehotel.features.dashboard.profile.add_franchise.AddFranchiseActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ManageFranchiseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageFranchiseBinding
    private val manageFranchiseViewModel: ManageFranchiseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManageFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleButtonAdd()

        setupActionBar()

        handleButtonBack()

        fetchListManageFranchise()

        handleRefresh()
    }

    private fun handleRefresh() {
        binding.refFranchiseHotel.setOnRefreshListener {
            fetchListManageFranchise()
        }
    }

    private fun fetchListManageFranchise() {
        manageFranchiseViewModel.getHotels().observe(this) { hotel ->
            when (hotel) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        showToast(hotel.message.toString())
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("ManageFranchiseActivity").d(hotel.message)
                }
                is Resource.Success -> {
                    showLoading(false)

                    initFranchiseRecyclerView(hotel.data)
                }
            }
        }
    }

    private fun initFranchiseRecyclerView(data: List<Hotel>?) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListFranchise.layoutManager = layoutManager

        val adapter = ManageHotelListAdapter(data)
        binding.rvListFranchise.adapter = adapter
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refFranchiseHotel.isRefreshing = isLoading
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