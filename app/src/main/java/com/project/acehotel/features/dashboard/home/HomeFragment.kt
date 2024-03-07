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
import com.project.acehotel.core.ui.adapter.booking.BookingListAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentHomeBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchListRoom()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun fetchListRoom() {
        val date = DateUtils.getDateThisMonth()

        homeViewModel.executeGetListBookingByHotel(date).observe(this) { booking ->
            when (booking) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(requireContext())) {
                        activity?.showToast(getString(R.string.check_internet))
                    } else {
                        activity?.showToast(booking.message.toString())
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
                }
            }
        }


//        homeViewModel.executeGetListRoomByHotel().observe(this) { room ->
//            when (room) {
//                is Resource.Error -> {
//                    showLoading(false)
//
//                    if (!isInternetAvailable(requireContext())) {
//                        activity?.showToast(getString(R.string.check_internet))
//                    } else {
//                        activity?.showToast(room.message.toString())
//                    }
//                }
//                is Resource.Loading -> {
//                    showLoading(true)
//                }
//                is Resource.Message -> {
//                    showLoading(false)
//                    Timber.tag("InventoryDetailActivity").d(room.message)
//                }
//                is Resource.Success -> {
//                    showLoading(false)
//
//                    initRoomRecyclerView(room.data)
//                }
//            }
//        }
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

    private fun showLoading(isLoading: Boolean) {
        binding.refHome.isRefreshing = isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
    }
}