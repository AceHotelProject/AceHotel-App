package com.project.acehotel.features.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.ui.adapter.booking.BookingListAdapter
import com.project.acehotel.core.ui.adapter.visitor.CurrentVisitorAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentHomeBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import com.project.acehotel.features.dashboard.management.visitor.detail.VisitorDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    private var hotelData: ManageHotel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchHotelData()

        fetchListBooking()
    }

    private fun fetchHotelData() {
        homeViewModel.getSelectedHotelData().observe(this) {
            hotelData = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun fetchListBooking() {
        val date = DateUtils.getDateThisDay()

        homeViewModel.executeGetListBookingByHotel(date).observe(this) { booking ->
            when (booking) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(requireContext())) {
                        activity?.showToast(getString(R.string.check_internet))
                    } else {
                        if (booking.message?.contains("404", false) == true) {
                            initBookingRecyclerView(listOf())
                        } else {
                            activity?.showToast(booking.message.toString())
                        }
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("InventoryDetailActivity").d(booking.message)
                }
                is Resource.Success -> {
                    showLoading(false)

                    initBookingRecyclerView(booking.data)

                    initCurrentVisitorRecyclerView(booking.data)

                    initTodayRecap(booking.data)
                }
            }
        }
    }

    private fun initTodayRecap(booking: List<Booking>?) {
        var todayRevenue = 0
        var todayVisitorCheckIn = 0
        var todayRoomUsed = 0
        var todayRoomAvailable = 0
        var todayNewBooking = 0

        if (booking != null) {
            for (data in booking) {
                todayRevenue += data.totalPrice

                if (data.room.isNotEmpty()) {
                    ++todayRoomUsed
                    if (data.room.first().actualCheckin != "Empty") {
                        ++todayVisitorCheckIn
                    } else if (data.room.first().actualCheckin == "Empty" && data.room.first().actualCheckout == "Empty") {
                        ++todayNewBooking
                    }
                }
            }

            todayRoomAvailable = hotelData?.roomId?.size!! - todayRoomUsed
        }

        binding.apply {
            tvTotalIncome.text = todayRevenue.toString()
            tvVisitorCheckin.text = todayVisitorCheckIn.toString()
            tvRoomAvail.text = todayRoomAvailable.toString()
            tvTotalNewBook.text = todayNewBooking.toString()
        }
    }

    private fun initBookingRecyclerView(booking: List<Booking>?) {
        val adapter = BookingListAdapter(booking)
        binding.rvListCurrentBook.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListCurrentBook.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : BookingListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, booking: Booking) {
                val intentToBookingDetail =
                    Intent(requireContext(), BookingDetailActivity::class.java)
                val dataToJson = Gson().toJson(booking)

                intentToBookingDetail.putExtra(BOOKING_DATA, dataToJson)
                startActivity(intentToBookingDetail)
            }
        })
    }

    private fun initCurrentVisitorRecyclerView(booking: List<Booking>?) {
        val adapter = CurrentVisitorAdapter(booking)
        binding.rvListCurrentVisitor.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListCurrentVisitor.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : CurrentVisitorAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String, name: String) {
                val intentToVisitorDetail =
                    Intent(requireContext(), VisitorDetailActivity::class.java)
                intentToVisitorDetail.putExtra(VISITOR_ID, id)
                startActivity(intentToVisitorDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refHome.isRefreshing = isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
        private const val VISITOR_ID = "visitor_id"
    }
}