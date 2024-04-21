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
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.adapter.booking.BookingPagingListAdapter
import com.project.acehotel.core.utils.IUserLayout
import com.project.acehotel.core.utils.constants.UserRole
import com.project.acehotel.core.utils.constants.filterDateList
import com.project.acehotel.core.utils.constants.mapToFilterDateValue
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentFinanceBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import com.project.acehotel.features.dashboard.management.IManagementSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FinanceFragment : Fragment(), IManagementSearch, IUserLayout {
    private var _binding: FragmentFinanceBinding? = null
    private val binding get() = _binding!!

    private val financeViewModel: FinanceViewModel by activityViewModels()

    private var selectedDate = mapToFilterDateValue(filterDateList.first())
    private var exclusiveRoomPrice: Int = 0
    private var regularRoomPrice: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchBookingHistory(selectedDate)

        fetchFinanceStat(selectedDate)

        setupFilter()

        handleRefresh()

        handleEmptyState(true)

        checkUserRole()
    }

    private fun checkUserRole() {
        financeViewModel.getUser().observe(requireActivity()) { user ->
            user.user?.role?.let { changeLayoutByUser(it) }
        }
    }

    private fun handleRefresh() {
        binding.apply {
            svFinance.viewTreeObserver.addOnScrollChangedListener {
                refFinance.isEnabled = svFinance.scrollY == 0
            }

            refFinance.setOnRefreshListener {
                fetchBookingHistory(selectedDate)
            }
        }
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

                    selectedDate = mapToFilterDateValue(p0.toString())

                    fetchBookingHistory(selectedDate)

                    fetchFinanceStat(selectedDate)
                }
            })
        }
    }

    private fun fetchFinanceStat(filterDate: String) {
        var totalRevenue = 0
        var totalBooking = 0

        financeViewModel.getSelectedHotelData().observe(this) { hotel ->
            exclusiveRoomPrice = hotel.exclusiveRoomPrice
            regularRoomPrice = hotel.regularRoomPrice
        }

        financeViewModel.executeGetListBookingByHotel(filterDate).observe(this) { booking ->
            when (booking) {
                is Resource.Error -> {
                    showLoading(false)
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("FinanceFragment").d(booking.message)
                }
                is Resource.Success -> {
                    showLoading(false)
                    Timber.tag("FINANCE").e(booking.data.toString())

                    if (booking.data != null) {

                        for (item in booking.data) {
                            if (item.room.first().actualCheckin != "Empty" && item.room.first().actualCheckout != "Empty") {
                                totalRevenue += item.totalPrice
                                totalBooking += item.roomCount
                            }
                        }

                        binding.apply {
                            tvFinanceTotalProfit.text = totalRevenue.toString()
                            tvFinanceTotalBooking.text = totalBooking.toString()
                        }
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

            val isEmpty = adapter.itemCount == 0
            handleEmptyState(isEmpty)
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
                val jsonData = Gson().toJson(booking, Booking::class.java)
                intentToBookingDetail.putExtra(BOOKING_DATA, jsonData)
                startActivity(intentToBookingDetail)
            }
        })
    }

    private fun handleEmptyState(isEmpty: Boolean) {
        binding.rvFinanceHistory.layoutParams.height =
            if (isEmpty) 400 else ViewGroup.LayoutParams.WRAP_CONTENT
        binding.tvEmptyBooking.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refFinance.isRefreshing = isLoading
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
    }

    override fun onSearchQuery(query: String) {
        activity?.showToast("Pencarian booking tidak dapat dilakukan, silahkan lakukan pencarian pada bagian halaman \"Pesan\"")
    }

    override fun changeLayoutByUser(userRole: UserRole) {
        when (userRole) {
            UserRole.MASTER -> {

            }
            UserRole.FRANCHISE -> {

            }
            UserRole.INVENTORY_STAFF -> {
                binding.mainLayout.visibility = View.GONE
            }
            UserRole.RECEPTIONIST -> {
                binding.mainLayout.visibility = View.GONE
            }
            UserRole.ADMIN -> {

            }
            UserRole.UNDEFINED -> {
                binding.mainLayout.visibility = View.GONE
            }
        }
    }
}