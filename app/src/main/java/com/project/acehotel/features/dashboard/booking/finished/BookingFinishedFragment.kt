package com.project.acehotel.features.dashboard.booking.finished

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.adapter.booking.BookingListAdapter
import com.project.acehotel.core.ui.adapter.booking.BookingPagingListAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.databinding.FragmentBookingFinishedBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingFinishedFragment : Fragment() {
    private var _binding: FragmentBookingFinishedBinding? = null
    private val binding get() = _binding!!

    private val bookingFinishedViewModel: BookingFinishedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchBookingPagingList()
    }

    private fun fetchBookingPagingList() {
        val adapter = BookingPagingListAdapter()
        binding.rvBookingFinished.adapter = adapter

        adapter.addLoadStateListener { loadStates ->
            val isRefreshing =
                loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
            binding.refBookingFinished.isRefreshing = isRefreshing
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookingFinished.layoutManager = layoutManager

        val dateNow = DateUtils.getDateThisYear()
        bookingFinishedViewModel.executeGetPagingListBookingByHotel(dateNow)
            .observe(this) { booking ->
                lifecycleScope.launch {
                    adapter.submitData(booking)
                }
            }
    }

    private fun initBookingRecyclerView(booking: List<Booking>?) {
        val adapter = BookingListAdapter(booking)
        binding.rvBookingFinished.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookingFinished.layoutManager = layoutManager

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
        binding.refBookingFinished.isRefreshing = isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
    }
}