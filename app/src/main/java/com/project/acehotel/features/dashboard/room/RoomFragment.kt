package com.project.acehotel.features.dashboard.room

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.domain.room.model.Room
import com.project.acehotel.core.ui.adapter.room.RoomListAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.IUserLayout
import com.project.acehotel.core.utils.constants.UserRole
import com.project.acehotel.core.utils.formatNumber
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentRoomBinding
import com.project.acehotel.features.dashboard.room.change_price.ChangePriceActivity
import com.project.acehotel.features.dashboard.room.detail.RoomDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RoomFragment : Fragment(), IUserLayout {
    private var _binding: FragmentRoomBinding? = null
    private val binding get() = _binding!!

    private val roomViewModel: RoomViewModel by activityViewModels()

    private var hotelData: ManageHotel? = null

    private var visitorCheckIn = 0
    private var visitorCheckOut = 0
    private var roomBooked = 0
    private var roomAvail = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleButtonChangePrice()

        fetchHotelInfo()

        fetchListRoom()

        handleRefresh()

        fetchRoomStats()

        checkUserRole()
    }

    private fun checkUserRole() {
        roomViewModel.getUser().observe(requireActivity()) { user ->
            user.user?.role?.let {
                changeLayoutByUser(it)
            }
        }
    }

    private fun fetchRoomStats() {
        val filterDate = DateUtils.getDateThisDay()

        roomViewModel.executeGetListBookingByHotel(filterDate)
            .observe(requireActivity()) { booking ->
                when (booking) {
                    is Resource.Error -> {
                        showLoading(false)
                        if (!isInternetAvailable(requireContext())) {
                            activity?.showToast(getString(R.string.check_internet))
                        } else {
                            Timber.tag("RoomFragment").e(booking.message)
                        }
                    }

                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Message -> {
                        showLoading(false)
                        Timber.tag("RoomFragment").d(booking.message)
                    }

                    is Resource.Success -> {
                        showLoading(false)

                        val result = booking.data
                        if (result != null) {
                            for (item in result) {
                                if (item.room.first().actualCheckin != "Empty" && item.room.first().actualCheckout != "Empty") {
                                    ++visitorCheckOut
                                } else if (item.room.first().actualCheckin != "Empty" && item.room.first().actualCheckout == "Empty") {
                                    ++visitorCheckIn
                                } else if (DateUtils.isTodayDate(item.checkinDate) && item.room.first().actualCheckout == "Empty") {
                                    ++roomBooked
                                } else {
                                    continue
                                }
                            }
                        }

                        binding.apply {
                            roomAvail =
                                hotelData?.regularRoomCount!! + hotelData?.exclusiveRoomCount!! - roomBooked

                            tvRoomTotalCheckin.text = visitorCheckIn.toString()
                            tvRoomTotalCheckout.text = visitorCheckOut.toString()
                            tvRoomTotalRoomAvail.text = roomAvail.toString()
                            tvRoomTotalRoomFull.text = roomBooked.toString()
                        }
                    }
                }
            }
    }

    private fun handleRefresh() {
        binding.apply {
            svRoom.viewTreeObserver.addOnScrollChangedListener {
                refRoom.isEnabled = svRoom.scrollY == 0
            }

            refRoom.setOnRefreshListener {
                fetchHotelInfo()

                fetchListRoom()
            }
        }
    }

    private fun fetchListRoom() {
        roomViewModel.executeGetListRoomByHotel().observe(requireActivity()) { room ->
            when (room) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(requireContext())) {
                        activity?.showToast(getString(R.string.check_internet))
                    } else {
                        activity?.showToast(room.message.toString())
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("RoomFragment").d(room.message)
                }

                is Resource.Success -> {
                    showLoading(false)

                    initRoomRecyclerView(room.data)
                }
            }
        }
    }

    private fun initRoomRecyclerView(data: List<Room>?) {
        val adapter = RoomListAdapter(data)
        binding.rvListRoom.adapter = adapter

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvListRoom.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : RoomListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, room: Room) {
                val intentToRoomDetail = Intent(requireContext(), RoomDetailActivity::class.java)

                val dataJson = Gson().toJson(room, Room::class.java)
                intentToRoomDetail.putExtra(ROOM_DATA, dataJson)
                activity?.startActivity(intentToRoomDetail)
            }
        })
    }

    private fun fetchHotelInfo() {
        roomViewModel.getSelectedHotelData().observe(requireActivity()) { hotel ->
            hotelData = hotel

            binding.apply {
//                roomAvail = hotel.regularRoomCount + hotel.exclusiveRoomCount

                tvRoomPriceExclusive.text = "Rp ${formatNumber(hotel.exclusiveRoomPrice)}"
                tvRoomPriceRegular.text = "Rp ${formatNumber(hotel.regularRoomPrice)}"

                tvRoomDiscount.text = if (hotel.discount == "Empty") "-" else hotel.discount
                tvRoomDiscountPrice.text = "Rp ${formatNumber(hotel.discountAmount)}"

                tvRoomBedPrice.text = "Rp ${formatNumber(hotel.extraBedPrice)}"
            }
        }
    }

    private fun handleButtonChangePrice() {
        binding.btnEditPrice.setOnClickListener {
            val intentToChangePrice = Intent(requireContext(), ChangePriceActivity::class.java)
            startActivity(intentToChangePrice)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refRoom.isRefreshing = isLoading
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val ROOM_DATA = "room_data"
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
                binding.btnEditPrice.visibility = View.GONE
            }

            UserRole.ADMIN -> {

            }

            UserRole.UNDEFINED -> {
                binding.mainLayout.visibility = View.GONE
            }
        }
    }
}