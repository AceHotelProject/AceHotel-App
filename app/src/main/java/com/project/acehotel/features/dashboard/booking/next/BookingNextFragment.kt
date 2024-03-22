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
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.adapter.booking.BookingPagingListAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.databinding.FragmentBookingNextBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingNextFragment : Fragment() {
    private var _binding: FragmentBookingNextBinding? = null
    private val binding get() = _binding!!

    private val bookingNextViewModel: BookingNextViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchBookingPagingList()
    }

    private fun fetchBookingPagingList() {
        val adapter = BookingPagingListAdapter()
        binding.rvBookingNext.adapter = adapter

        adapter.addLoadStateListener { loadStates ->
            val isRefreshing =
                loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
            binding.refBookingNext.isRefreshing = isRefreshing
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookingNext.layoutManager = layoutManager

        val filterDate = DateUtils.getDateThisYear()
        bookingNextViewModel.executeGetPagingListBookingByHotel(filterDate, false)
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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
    }
}