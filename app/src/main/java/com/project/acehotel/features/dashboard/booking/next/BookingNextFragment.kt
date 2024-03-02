package com.project.acehotel.features.dashboard.booking.next

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.adapter.booking.BookingListAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentBookingNextBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BookingNextFragment : Fragment() {
    private var _binding: FragmentBookingNextBinding? = null
    private val binding get() = _binding!!

    private val bookingNextViewModel: BookingNextViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchBookingList()
    }

    private fun fetchBookingList() {
        val dateNow = DateUtils.getDateThisYear()

        bookingNextViewModel.executeGetListBookingByHotel(dateNow).observe(this) { booking ->
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
                    Timber.tag("BookingNowFragment").d(booking.message)
                }
                is Resource.Success -> {
                    showLoading(false)
                    initBookingRecyclerView(booking.data)
                }
            }
        }
    }

    private fun initBookingRecyclerView(booking: List<Booking>?) {
        val adapter = BookingListAdapter(booking)
        binding.rvBookingNext.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookingNext.layoutManager = layoutManager

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
        binding.refBookingNext.isRefreshing = isLoading
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingNextBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
    }
}