package com.project.acehotel.features.dashboard.profile.choose_hotel

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.ListHotel
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

    private var selectedHotel: String = ""

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
    }

    private fun checkSelectedHotel() {
        chooseHotelViewModel.getSelectedHotel().observe(this) { hotel ->

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
                }
            }
        }
    }

    private fun initInventoryRecyclerView(data: List<ListHotel>?) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListChooseHotel.layoutManager = layoutManager

        val adapter = HotelListAdapter(data, selectedHotel)
        binding.rvListChooseHotel.adapter = adapter

        adapter.setOnItemClickCallback(object : HotelListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, id: String) {
                chooseHotelViewModel.saveSelectedHotel(id)
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