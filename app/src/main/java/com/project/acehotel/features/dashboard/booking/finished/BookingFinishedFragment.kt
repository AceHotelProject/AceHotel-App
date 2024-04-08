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
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.adapter.booking.BookingPagingListAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.databinding.FragmentBookingFinishedBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import com.project.acehotel.features.dashboard.management.IManagementSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingFinishedFragment : Fragment(), IManagementSearch {
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

        binding.tvEmptyBookingFinish.visibility = View.VISIBLE

        fetchBookingPagingList("")

        handleRefresh()
    }

    private fun handleRefresh() {
        binding.refBookingFinished.setOnRefreshListener {
            fetchBookingPagingList("")
        }
    }

    private fun fetchBookingPagingList(visitorName: String) {
        val adapter = BookingPagingListAdapter()
        binding.rvBookingFinished.adapter = adapter

        adapter.addLoadStateListener { loadStates ->
            val isRefreshing =
                loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
            binding.refBookingFinished.isRefreshing = isRefreshing
            val isEmpty = adapter.itemCount == 0

            handleEmptyStates(isEmpty)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookingFinished.layoutManager = layoutManager

        val filterDate = DateUtils.getDateThisYear()
        bookingFinishedViewModel.executeGetPagingListBookingByHotel(filterDate, true, visitorName)
            .observe(this) { booking ->
                lifecycleScope.launch {
                    adapter.submitData(booking)
                }
            }

        adapter.setOnItemClickCallback(object : BookingPagingListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, booking: Booking) {
                val intentToBookingDetail =
                    Intent(requireContext(), BookingDetailActivity::class.java)
                intentToBookingDetail.putExtra(BOOKING_DATA, booking)
                startActivity(intentToBookingDetail)
            }
        })
    }

    private fun handleEmptyStates(isEmpty: Boolean) {
        binding.tvEmptyBookingFinish.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
    }

    override fun onSearchQuery(query: String) {
        fetchBookingPagingList(query)
    }
}