package com.project.acehotel.features.dashboard.profile.choose_hotel

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.ui.adapter.hotel.HotelListAdapter
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityChooseHotelBinding
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChooseHotelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseHotelBinding
    private val chooseHotelViewModel: ChooseHotelViewModel by viewModels()

    private var selectedHotel: ManageHotel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        checkSelectedHotel()

        showLoading(false)

        fetchListHotel()

        validateToken()

        handleRefresh()

        binding.tvEmptyHotel.visibility = View.VISIBLE
    }

    private fun handleRefresh() {
        binding.refChooseHotel.setOnRefreshListener {
            fetchListHotel()
        }
    }

    private fun checkSelectedHotel() {
        chooseHotelViewModel.getSelectedHotelData().observe(this) { hotel ->

            selectedHotel = hotel
        }
    }

    private fun fetchListHotel() {
        chooseHotelViewModel.getHotels().observe(this) { hotel ->
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
                    Timber.tag("ChooseHotelActivity").d(hotel.message)
                }
                is Resource.Success -> {
                    showLoading(false)

                    initInventoryRecyclerView(hotel.data)

                    handleEmptyStates(hotel.data)
                }
            }
        }
    }

    private fun handleEmptyStates(data: List<ManageHotel>?) {
        binding.tvEmptyHotel.visibility = if (data?.isEmpty()!!) View.VISIBLE else View.GONE
    }

    private fun initInventoryRecyclerView(data: List<ManageHotel>?) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListChooseHotel.layoutManager = layoutManager

        val adapter = HotelListAdapter(data, selectedHotel!!)
        binding.rvListChooseHotel.adapter = adapter

        adapter.setOnItemClickCallback(object : HotelListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, data: ManageHotel) {
                chooseHotelViewModel.saveSelectedHotelData(data)
            }
        })
    }

    private fun validateToken() {
        chooseHotelViewModel.getRefreshToken().observe(this) { token ->
            if (token.isNullOrEmpty()) {
                TokenExpiredDialog().show(supportFragmentManager, "Token Expired Dialog")
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refChooseHotel.isRefreshing = isLoading
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}