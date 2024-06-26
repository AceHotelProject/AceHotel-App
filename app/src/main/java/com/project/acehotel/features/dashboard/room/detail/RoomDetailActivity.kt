package com.project.acehotel.features.dashboard.room.detail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.room.model.Room
import com.project.acehotel.core.ui.adapter.booking.BookingListAdapter
import com.project.acehotel.core.ui.adapter.visitor.CurrentVisitorAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.IUserLayout
import com.project.acehotel.core.utils.constants.DeleteDialogType
import com.project.acehotel.core.utils.constants.RoomStatus
import com.project.acehotel.core.utils.constants.RoomType
import com.project.acehotel.core.utils.constants.UserRole
import com.project.acehotel.core.utils.full_image_view.FullImageViewActivity
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityRoomDetailBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import com.project.acehotel.features.dashboard.management.visitor.detail.VisitorDetailActivity
import com.project.acehotel.features.popup.delete.DeleteItemDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RoomDetailActivity : AppCompatActivity(), IUserLayout {
    private lateinit var binding: ActivityRoomDetailBinding

    private val roomDetailViewModel: RoomDetailViewModel by viewModels()

    private var roomData: Room? = null
    private var roomImage: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoomDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleBackButton()

        fetchRoomData()

        fetchListBooking()

        handleRefresh()

        handleButtonMore()

        handleImageView()

        checkUserRole()

        handleEmptyStates(listOf(), binding.rvRoomDetailNextBooking)
        handleEmptyStates(listOf(), binding.rvRoomDetailCurrentVisitor)
    }

    private fun checkUserRole() {
        roomDetailViewModel.getUser().observe(this) { user ->
            user.user?.role?.let { changeLayoutByUser(it) }
        }
    }

    private fun handleImageView() {
        binding.ivRoomDetail.setOnClickListener {
            if (roomImage.isNotEmpty()) {
                val intentToFullImageView =
                    Intent(this@RoomDetailActivity, FullImageViewActivity::class.java)
                intentToFullImageView.putExtra(IMAGE_SOURCE, roomImage)
                startActivity(intentToFullImageView)
            }
        }
    }

    private fun handleButtonMore() {
        binding.btnMore.setOnClickListener {
            val popUpMenu = PopupMenu(this, binding.btnMore)
            popUpMenu.menuInflater.inflate(R.menu.menu_detail_delete_item, popUpMenu.menu)

            popUpMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuDelete -> {
                        if (roomData != null) {
                            DeleteItemDialog(
                                DeleteDialogType.ROOM_DETAIL,
                                roomData!!.id
                            ).show(supportFragmentManager, "Room Dialog")
                        }

                        true
                    }

                    else -> false
                }
            }

            popUpMenu.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleRefresh() {
        binding.apply {
            svRoomDetail.viewTreeObserver.addOnScrollChangedListener {
                refRoomDetail.isEnabled = svRoomDetail.scrollY == 0
            }

            refRoomDetail.setOnRefreshListener {
                fetchRoomData()

                fetchListBooking()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchListBooking() {
        val date = DateUtils.getDateThisYear()
        val roomId = roomData?.id

        roomDetailViewModel.getListBookingByRoom(roomId!!, date).observe(this) { booking ->
            when (booking) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        if (booking.message?.contains("404", false) == true) {
                            initBookingRecyclerView(listOf())
                        } else {
                            showToast(booking.message.toString())
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
                }
            }
        }
    }

    private fun handleEmptyStates(data: List<Booking>?, recyclerView: RecyclerView) {
        if (data != null) {
            recyclerView.layoutParams.height =
                if (data.isEmpty()) 400 else ViewGroup.LayoutParams.WRAP_CONTENT

            binding.apply {
                when (recyclerView) {
                    rvRoomDetailCurrentVisitor -> {
                        tvEmptyCurrentVisitor.visibility =
                            if (data.isEmpty()) View.VISIBLE else View.INVISIBLE
                    }
                    rvRoomDetailNextBooking -> {
                        tvEmptyCurrentBook.visibility =
                            if (data.isEmpty()) View.VISIBLE else View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun fetchRoomData() {
        val data = intent.getStringExtra(ROOM_DATA)
        roomData = Gson().fromJson(data, Room::class.java)

        binding.apply {
            if (roomData != null) {
                chipRoomType.setStatus(roomData!!.type)
                tvRoomDetailName.text = roomData!!.name

                if (!roomData!!.isBooked) {
                    updateStatus(RoomStatus.READY.status)
                } else {
                    if (!isRoomBookings(roomData!!.bookings)) {
                        updateStatus(RoomStatus.READY.status)
                    } else {
                        updateStatus(RoomStatus.USED.status)
                    }
                }
            }
        }

        roomDetailViewModel.getSelectedHotelData().observe(this) { hotel ->
            roomImage = when (roomData!!.type) {
                RoomType.REGULAR.type -> {
                    hotel.regularRoomImage
                }
                RoomType.EXCLUSIVE.type -> {
                    hotel.exclusiveRoomImage
                }
                else -> {
                    hotel.regularRoomImage
                }
            }

            Glide.with(this).load(roomImage).into(binding.ivRoomDetail)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initBookingRecyclerView(booking: List<Booking>?) {
        val filterBooking = booking?.filter {
            DateUtils.isDateAfterToday(it.checkinDate) && (it.room.first().actualCheckin == "Empty" && it.room.first().actualCheckout == "Empty")
        }

        val adapter = BookingListAdapter(filterBooking)
        binding.rvRoomDetailNextBooking.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvRoomDetailNextBooking.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : BookingListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, booking: Booking) {
                val intentToBookingDetail =
                    Intent(this@RoomDetailActivity, BookingDetailActivity::class.java)
                val dataToJson = Gson().toJson(booking)

                intentToBookingDetail.putExtra(BOOKING_DATA, dataToJson)
                startActivity(intentToBookingDetail)
            }
        })

        handleEmptyStates(filterBooking, binding.rvRoomDetailNextBooking)
    }

    private fun initCurrentVisitorRecyclerView(booking: List<Booking>?) {
        val filterBooking = booking?.filter {
            DateUtils.isTodayDate(it.checkinDate)
        }

        val adapter = CurrentVisitorAdapter(filterBooking)
        binding.rvRoomDetailCurrentVisitor.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvRoomDetailCurrentVisitor.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : CurrentVisitorAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String, name: String) {
                val intentToVisitorDetail =
                    Intent(this@RoomDetailActivity, VisitorDetailActivity::class.java)
                intentToVisitorDetail.putExtra(VISITOR_ID, id)
                startActivity(intentToVisitorDetail)
            }
        })

        handleEmptyStates(filterBooking, binding.rvRoomDetailCurrentVisitor)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refRoomDetail.isRefreshing = isLoading
    }

    private fun handleBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun updateStatus(status: String) {
        when (status) {
            RoomStatus.READY.status -> {
                statusDisplay(
                    RoomStatus.READY.display,
                    R.color.green,
                    R.drawable.icons_room_card_avail
                )
            }
            RoomStatus.USED.status -> {
                statusDisplay(
                    RoomStatus.USED.display,
                    R.color.red,
                    R.drawable.icons_room_card_not_avail
                )
            }
            RoomStatus.BROKEN.status -> {
                statusDisplay(
                    RoomStatus.READY.display,
                    R.color.dark_grey,
                    R.drawable.icons_room_card_broken
                )
            }
            else -> {
                statusDisplay(
                    RoomStatus.UNDEFINED.display,
                    R.color.dark_grey,
                    R.drawable.icons_room_card_broken
                )
            }
        }
    }

    private fun statusDisplay(display: String, colorId: Int, imageId: Int) {
        findViewById<TextView>(R.id.tv_room_detail_status).apply {
            text = display

            val color = ContextCompat.getColor(context, colorId)
            setTextColor(color)
        }

        findViewById<ImageView>(R.id.iv_room_detail_status).setImageResource(imageId)
    }

    private fun isRoomBookings(bookings: List<Booking>): Boolean {
        for (booking in bookings) {
            return DateUtils.isTodayDate(booking.checkinDate)
        }

        return true
    }

    companion object {
        private const val ROOM_DATA = "room_data"

        private const val BOOKING_DATA = "booking_data"
        private const val VISITOR_ID = "visitor_id"

        private const val IMAGE_SOURCE = "image_source"
    }

    override fun changeLayoutByUser(userRole: UserRole) {
        when (userRole) {
            UserRole.MASTER -> {

            }
            UserRole.FRANCHISE -> {

            }
            UserRole.INVENTORY_STAFF -> {
                binding.btnMore.visibility = View.GONE
            }
            UserRole.RECEPTIONIST -> {
                binding.btnMore.visibility = View.GONE
            }
            UserRole.ADMIN -> {

            }
            UserRole.UNDEFINED -> {
                binding.btnMore.visibility = View.GONE
            }
        }
    }

}