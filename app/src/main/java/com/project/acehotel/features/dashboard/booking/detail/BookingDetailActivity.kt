package com.project.acehotel.features.dashboard.booking.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.DeleteDialogType
import com.project.acehotel.core.utils.constants.FabMenuState
import com.project.acehotel.core.utils.constants.RoomType
import com.project.acehotel.core.utils.constants.mapToRoomDisplay
import com.project.acehotel.core.utils.formatNumber
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityBookingDetailBinding
import com.project.acehotel.features.dashboard.booking.add_booking.AddBookingActivity
import com.project.acehotel.features.dashboard.booking.choose_booking.ChooseBookingActivity
import com.project.acehotel.features.popup.delete.DeleteItemDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BookingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailBinding

    private val bookingDetailViewModel: BookingDetailViewModel by viewModels()

    private var bookingData: Booking? = null

    private var fabMenuState: FabMenuState = FabMenuState.COLLAPSED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        disableRefresh()

        initBookingData()

        handleButtonMore()

        handleBackButton()

        handleFab()

        disableRefresh()
    }

    private fun handleButtonMore() {
        binding.btnMore.setOnClickListener {
            val popUpMenu = PopupMenu(this, binding.btnMore)
            popUpMenu.menuInflater.inflate(R.menu.menu_detail_item, popUpMenu.menu)

            popUpMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuUpdate -> {
                        val intentToUpdateItem =
                            Intent(this, AddBookingActivity::class.java)

                        startActivity(intentToUpdateItem)
                        true
                    }
                    R.id.menuDelete -> {
                        if (bookingData != null) {
                            DeleteItemDialog(
                                DeleteDialogType.BOOKING_DETAIL,
                                bookingData!!.id
                            ).show(
                                supportFragmentManager,
                                "Delete Dialog"
                            )
                        }

                        true
                    }
                    else -> false
                }
            }

            popUpMenu.show()
        }
    }

    private fun handleBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initBookingData() {
        val data = intent.getStringExtra(BOOKING_DATA)
        bookingData = Gson().fromJson(data, Booking::class.java)

        binding.apply {
            if (bookingData != null) {
                bookingDetailViewModel.getRoomDetail(bookingData!!.room.first().id)
                    .observe(this@BookingDetailActivity) { room ->
                        when (room) {
                            is Resource.Error -> {
                                showLoading(false)

                                if (!isInternetAvailable(this@BookingDetailActivity)) {
                                    showToast(getString(R.string.check_internet))
                                } else {
                                    showToast(room.message.toString())
                                }
                            }
                            is Resource.Loading -> {
                                showLoading(true)
                            }
                            is Resource.Message -> {
                                showLoading(false)
                                Timber.tag("BookingDetailActivity").d(room.message)
                            }
                            is Resource.Success -> {
                                chipRoomCardType.setStatus(room.data?.type ?: "type")
                                tvRoomCardName.text = room.data?.name

                            }
                        }
                    }

                bookingDetailViewModel.getVisitorDetail(bookingData!!.visitorId)
                    .observe(this@BookingDetailActivity) { visitor ->
                        when (visitor) {
                            is Resource.Error -> {
                                showLoading(false)

                                if (!isInternetAvailable(this@BookingDetailActivity)) {
                                    showToast(getString(R.string.check_internet))
                                } else {
                                    showToast(visitor.message.toString())
                                }
                            }
                            is Resource.Loading -> {
                                showLoading(true)
                            }
                            is Resource.Message -> {
                                showLoading(false)
                                Timber.tag("BookingDetailActivity").d(visitor.message)
                            }
                            is Resource.Success -> {
                                showLoading(false)

                                tvVisitorDetailName.text = visitor.data?.name
                                tvVisitorDetailNik.text = visitor.data?.identity_num
                                tvVisitorDetailPhone.text = visitor.data?.phone
                                tvVisitorDetailEmail.text = visitor.data?.email

                                if (visitor.data?.identityImage != PLACEHOLDER_IMAGE) {
                                    Glide.with(this@BookingDetailActivity)
                                        .load(visitor.data?.identityImage).to(ivConfirmVisitor)
                                }
                            }
                        }
                    }

                tvConfirmCheckinDate.text =
                    DateUtils.convertToDisplayDateFormat2(bookingData!!.checkinDate)
                tvConfirmCheckoutDate.text =
                    DateUtils.convertToDisplayDateFormat2(bookingData!!.checkoutDate)

                tvConfirmNightCount.text = "${bookingData!!.duration} malam"
                tvConfirmRoomBook.text =
                    "${mapToRoomDisplay(bookingData!!.type)} (${bookingData!!.roomCount} kamar)"

                tvConfirmTotalPrice.text = "Rp ${formatNumber(bookingData!!.totalPrice)}"

                bookingDetailViewModel.getSelectedHotelData()
                    .observe(this@BookingDetailActivity) { hotel ->
                        if (bookingData!!.type == RoomType.REGULAR.type) {
                            tvConfirmRoom.text = mapToRoomDisplay(bookingData!!.type)
                            tvConfirmRoomDesc.text =
                                "${bookingData!!.duration} malam x ${bookingData!!.roomCount} kamar"
                            tvConfirmRoomPrice.text =
                                "Rp ${formatNumber(bookingData!!.duration * bookingData!!.roomCount * hotel.regularRoomPrice)}"

                            tvConfirmBedDesc.text = "${bookingData!!.addOn.size} unit"
                            tvConfirmBedPrice.text =
                                "Rp ${formatNumber(hotel.extraBedPrice * bookingData!!.addOn.size)}"

                            if ((bookingData!!.totalPrice - (bookingData!!.duration * bookingData!!.roomCount * hotel.regularRoomPrice)) == hotel.discountAmount) {
                                tvConfirmDiscDesc.text = "${hotel.discount}"
                                tvConfirmDiscPrice.text = "Rp ${hotel.discount}"
                            } else {
                                tvConfirmDiscDesc.text = "Tidak Menggunakan Diskon"
                                tvConfirmDiscPrice.text = "Rp 0"
                            }
                        } else if (bookingData!!.type == RoomType.EXCLUSIVE.type) {
                            tvConfirmRoom.text = mapToRoomDisplay(bookingData!!.type)
                            tvConfirmRoomDesc.text =
                                "${bookingData!!.duration} malam x ${bookingData!!.roomCount} kamar"
                            tvConfirmRoomPrice.text =
                                "Rp ${formatNumber(bookingData!!.duration * bookingData!!.roomCount * hotel.exclusiveRoomPrice)}"

                            tvConfirmBedDesc.text = "${bookingData!!.addOn.size} unit"
                            tvConfirmBedPrice.text =
                                "Rp ${formatNumber(hotel.extraBedPrice * bookingData!!.addOn.size)}"

                            if ((bookingData!!.totalPrice - (bookingData!!.duration * bookingData!!.roomCount * hotel.regularRoomPrice)) == hotel.discountAmount) {
                                tvConfirmDiscDesc.text = "${hotel.discount}"
                                tvConfirmDiscPrice.text = "Rp ${hotel.discount}"
                            } else {
                                tvConfirmDiscDesc.text = "Tidak Menggunakan Diskon"
                                tvConfirmDiscPrice.text = "Rp 0"
                            }
                        }

                        if (bookingData!!.transactionProof != PLACEHOLDER_IMAGE) {
                            Glide.with(this@BookingDetailActivity)
                                .load(bookingData!!.transactionProof).to(ivConfirmVisitor)
                        }
                    }
            }
        }
    }

    private fun handleFab() {
        binding.fabMenu.setOnClickListener {
            onFabMenuClick()
        }

        binding.fabVisitorCheckin.setOnClickListener {
            val intentToChooseBooking = Intent(this, ChooseBookingActivity::class.java)
            intentToChooseBooking.putExtra(FLAG_VISITOR, MENU_CHECKIN)
            startActivity(intentToChooseBooking)
        }

        binding.fabVisitorCheckout.setOnClickListener {
            val intentToChooseBooking = Intent(this, ChooseBookingActivity::class.java)
            intentToChooseBooking.putExtra(FLAG_VISITOR, MENU_CHECKOUT)
            startActivity(intentToChooseBooking)
        }
    }

    private fun onFabMenuClick() {
        fabMenuState = if (fabMenuState == FabMenuState.COLLAPSED) {
            FabMenuState.EXPANDED
        } else {
            FabMenuState.COLLAPSED
        }
        setVisibility(fabMenuState == FabMenuState.EXPANDED)
        setAnimation(fabMenuState == FabMenuState.EXPANDED)
        setClickable(fabMenuState == FabMenuState.EXPANDED)
    }

    private fun setAnimation(isClicked: Boolean) {
        val rotateOpen: Animation by lazy {
            AnimationUtils.loadAnimation(this, R.anim.anim_rotate_open)
        }
        val rotateClose: Animation by lazy {
            AnimationUtils.loadAnimation(this, R.anim.anim_rotate_close)
        }
        val fromBottom: Animation by lazy {
            AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom)
        }
        val toBottom: Animation by lazy {
            AnimationUtils.loadAnimation(this, R.anim.anim_to_bottom)
        }

        if (isClicked) {
            binding.fabMenu.startAnimation(rotateOpen)

            binding.fabVisitorCheckin.startAnimation(fromBottom)
            binding.tvVisitorCheckin.startAnimation(fromBottom)

            binding.fabVisitorCheckout.startAnimation(fromBottom)
            binding.tvVisitorCheckout.startAnimation(fromBottom)
        } else {
            binding.fabMenu.startAnimation(rotateClose)

            binding.fabVisitorCheckin.startAnimation(toBottom)
            binding.tvVisitorCheckin.startAnimation(toBottom)

            binding.fabVisitorCheckout.startAnimation(toBottom)
            binding.tvVisitorCheckout.startAnimation(toBottom)
        }
    }

    private fun setVisibility(isClicked: Boolean) {
        if (isClicked) {
            binding.fabVisitorCheckin.visibility = View.VISIBLE
            binding.tvVisitorCheckin.visibility = View.VISIBLE

            binding.fabVisitorCheckout.visibility = View.VISIBLE
            binding.tvVisitorCheckout.visibility = View.VISIBLE
        } else {
            binding.fabVisitorCheckin.visibility = View.INVISIBLE
            binding.tvVisitorCheckin.visibility = View.INVISIBLE

            binding.fabVisitorCheckout.visibility = View.INVISIBLE
            binding.tvVisitorCheckout.visibility = View.INVISIBLE
        }
    }

    private fun setClickable(isClicked: Boolean) {
        if (isClicked) {
            binding.fabVisitorCheckin.isClickable = true
            binding.fabVisitorCheckout.isClickable = true
        } else {
            binding.fabVisitorCheckin.isClickable = false
            binding.fabVisitorCheckout.isClickable = false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refConfirm.isRefreshing = isLoading
    }

    private fun disableRefresh() {
        binding.refConfirm.isEnabled = false
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
        private const val PLACEHOLDER_IMAGE =
            "https://storage.googleapis.com/ace-hotel/codioful-formerly-gradienta-G084bO4wGDA-unsplash.jpg"

        private const val FLAG_VISITOR = "flag_visitor"

        private const val MENU_CHECKIN = "checkin"
        private const val MENU_CHECKOUT = "checkout"
    }
}