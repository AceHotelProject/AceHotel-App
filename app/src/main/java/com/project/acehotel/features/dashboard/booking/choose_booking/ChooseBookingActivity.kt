package com.project.acehotel.features.dashboard.booking.choose_booking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.adapter.booking.BookingListAdapter
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityChooseBookingBinding
import com.project.acehotel.features.dashboard.MainActivity
import com.project.acehotel.features.dashboard.management.visitor.choose.ChooseVisitorActivity
import com.project.acehotel.features.dashboard.room.checkin.CheckinActivity
import com.project.acehotel.features.dashboard.room.checkout.CheckoutActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChooseBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseBookingBinding

    private val chooseBookingViewModel: ChooseBookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.tvEmptyBooking.visibility = View.VISIBLE

        handleButtonBack()

        fetchListBooking()

        handleRefresh()

        handleAddButton()

        handleSearch()
    }

    private fun handleSearch() {
        binding.edBookingSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                fetchListBooking(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                fetchListBooking(p0.toString())
            }
        })

        binding.edBookingSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fetchListBooking(binding.edBookingSearch.text.toString())
                true // consume the action
            } else {
                false // pass on to other listeners.
            }
        }
    }

    private fun handleAddButton() {
        binding.btnAddBooking.setOnClickListener {
            val intentToChooseVisitor = Intent(this, ChooseVisitorActivity::class.java)
            startActivity(intentToChooseVisitor)
        }
    }

    private fun handleRefresh() {
        binding.refChooseBooking.setOnRefreshListener {
            fetchListBooking()
        }
    }

    private fun fetchListBooking(visitorName: String = "") {
        chooseBookingViewModel.executeGetListBookingToday(visitorName).observe(this) { booking ->
            when (booking) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this@ChooseBookingActivity)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        if (booking.message?.contains("404", false) == true) {
                            initListBookingRecyclerView(listOf())
                        } else {
                            showToast(booking.message.toString())
                        }
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("ChooseBookingActivity").d(booking.message)
                }
                is Resource.Success -> {
                    showLoading(false)



                    when (intent.getStringExtra(FLAG_VISITOR)) {
                        MENU_CHECKIN -> {
                            val filterCheckinData = booking.data?.filter {
                                it.room.first().actualCheckin == "Empty"
                            }
                            initListBookingRecyclerView(filterCheckinData)

                            handleEmptyData(filterCheckinData)
                        }
                        MENU_CHECKOUT -> {
                            val filterCheckoutData = booking.data?.filter {
                                it.room.first().actualCheckin != "Empty" && it.room.first().actualCheckout == "Empty"
                            }
                            initListBookingRecyclerView(filterCheckoutData)

                            handleEmptyData(filterCheckoutData)
                        }
                    }
                }
            }
        }
    }

    private fun handleEmptyData(filterCheckinData: List<Booking>?) {
        binding.tvEmptyBooking.visibility =
            if (filterCheckinData?.isEmpty()!!) View.VISIBLE else View.GONE
    }

    private fun initListBookingRecyclerView(data: List<Booking>?) {
        val adapter = BookingListAdapter(data)
        binding.rvChooseBooking.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvChooseBooking.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : BookingListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, booking: Booking) {

                val intent: Intent = when (intent.getStringExtra(FLAG_VISITOR)) {
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

                val dataJson = Gson().toJson(booking)
                intent.putExtra(BOOKING_DATA, dataJson)
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