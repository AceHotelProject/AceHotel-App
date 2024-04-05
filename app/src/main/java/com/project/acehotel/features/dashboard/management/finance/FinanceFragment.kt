package com.project.acehotel.features.dashboard.management.finance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.adapter.booking.BookingPagingListAdapter
import com.project.acehotel.core.utils.constants.filterDateList
import com.project.acehotel.core.utils.constants.mapToFilterDateValue
import com.project.acehotel.databinding.FragmentFinanceBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import com.project.acehotel.features.dashboard.management.IManagementSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FinanceFragment : Fragment(), IManagementSearch {
    private var _binding: FragmentFinanceBinding? = null
    private val binding get() = _binding!!

    private val financeViewModel: FinanceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFilter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupFilter() {
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_inventory_item, filterDateList)

        binding.edAddItemType.apply {
            setAdapter(adapter)

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    binding.layoutAddItemType.isHintEnabled = p0.isNullOrEmpty()

                    fetchBookingHistory(mapToFilterDateValue(p0.toString()))

                    fetchFinanceStat(mapToFilterDateValue(p0.toString()))
                }
            })
        }
    }

    private fun fetchFinanceStat(filterDate: String) {
        financeViewModel.getHotelRecap(filterDate).observe(this) { recap ->
            when (recap) {
                is Resource.Error -> {
                    showLoading(false)
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("FinanceFragment").d(recap.message)
                }
                is Resource.Success -> {
                    showLoading(false)
                    binding.apply {
                        tvFinanceTotalProfit.text = recap.data?.revenue.toString()
                        tvFinanceTotalBooking.text = recap.data?.totalBooking.toString()
                    }
                }
            }
        }
    }


    private fun fetchBookingHistory(filterDate: String) {
        val adapter = BookingPagingListAdapter()
        binding.rvFinanceHistory.adapter = adapter

        adapter.addLoadStateListener { loadStates ->
            val isRefreshing =
                loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
            binding.refFinance.isRefreshing = isRefreshing
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinanceHistory.layoutManager = layoutManager

        financeViewModel.executeGetPagingListBookingByHotel(filterDate, true, "")
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
        binding.refFinance.isRefreshing = isLoading
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
    }

    override fun onSearchQuery(query: String) {

    }
}