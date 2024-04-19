package com.project.acehotel.features.dashboard.booking.next

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
import com.project.acehotel.core.ui.adapter.booking.BookingPagingListAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.databinding.FragmentBookingNextBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import com.project.acehotel.features.dashboard.management.IManagementSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingNextFragment : Fragment(), IManagementSearch {
    private var _binding: FragmentBookingNextBinding? = null
    private val binding get() = _binding!!

    private val bookingNextViewModel: BookingNextViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvEmptyBookingNext.visibility = View.VISIBLE

        fetchBookingPagingList("")

        handleRefresh()
    }

    private fun handleRefresh() {
        binding.refBookingNext.setOnRefreshListener {
            fetchBookingPagingList("")
        }
    }

    private fun fetchBookingPagingList(visitorName: String) {
        val adapter = BookingPagingListAdapter()
        binding.rvBookingNext.adapter = adapter

        adapter.addLoadStateListener { loadStates ->
            val isRefreshing =
                loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
            binding.refBookingNext.isRefreshing = isRefreshing
            val isEmpty = adapter.itemCount == 0

            handleEmptyStates(isEmpty)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookingNext.layoutManager = layoutManager

        val filterDate = DateUtils.getDateThisMonth()
        bookingNextViewModel.executeGetPagingListBookingByHotel(filterDate, false, visitorName)
            .observe(this) { booking ->
                lifecycleScope.launch {
                    adapter.submitData(booking)
                }
            }

        adapter.setOnItemClickCallback(object : BookingPagingListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, booking: Booking) {
                val intentToBookingDetail =
                    Intent(requireContext(), BookingDetailActivity::class.java)
                val jsonData = Gson().toJson(booking, Booking::class.java)
                intentToBookingDetail.putExtra(BOOKING_DATA, jsonData)
                startActivity(intentToBookingDetail)
            }
        })
    }

    private fun handleEmptyStates(isEmpty: Boolean) {
        binding.tvEmptyBookingNext.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingNextBinding.inflate(inflater, container, false)
        return binding.root
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