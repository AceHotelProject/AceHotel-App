package com.project.acehotel.features.dashboard.booking.choose_booking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.adapter.booking.BookingListAdapter
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityChooseBookingBinding
import com.project.acehotel.features.dashboard.MainActivity
import com.project.acehotel.features.dashboard.room.checkin.CheckinActivity
import com.project.acehotel.features.dashboard.room.checkout.CheckoutActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseBookingBinding

    private val chooseBookingViewModel: ChooseBookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        fetchListBooking()

        handleRefresh()
    }

    private fun handleRefresh() {
        binding.refChooseBooking.setOnRefreshListener {
            fetchListBooking()
        }
    }

    private fun fetchListBooking() {
        chooseBookingViewModel.executeGetListBookingToday().observe(this) { booking ->
            when (booking) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this@ChooseBookingActivity)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        showToast(booking.message.toString())
                    }

                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                }
                is Resource.Success -> {
                    showLoading(false)

                    initListBookingRecyclerView(booking.data)
                }
            }
        }
    }

    private fun initListBookingRecyclerView(data: List<Booking>?) {
        val adapter = BookingListAdapter(data)
        binding.rvChooseBooking.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvChooseBooking.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : BookingListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, booking: Booking) {
                val status = intent.getStringExtra(FLAG_VISITOR)

                val intent: Intent = when (status) {
                    MENU_CHECKIN -> {
                        Intent(this@ChooseBookingActivity, CheckinActivity::class.java)
                    }
                    MENU_CHECKOUT -> {
                        Intent(this@ChooseBookingActivity, CheckoutActivity::class.java)
                    }
                    else -> {
                        Intent(this@ChooseBookingActivity, MainActivity::class.java)
                    }
                }

                intent.putExtra(BOOKING_DATA, booking)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refChooseBooking.isRefreshing = isLoading
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val FLAG_VISITOR = "flag_visitor"
        private const val BOOKING_DATA = "booking_data"

        private const val MENU_CHECKIN = "checkin"
        private const val MENU_CHECKOUT = "checkout"


    }
}