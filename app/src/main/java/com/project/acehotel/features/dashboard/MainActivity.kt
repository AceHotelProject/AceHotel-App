package com.project.acehotel.features.dashboard

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.FabMenuState
import com.project.acehotel.core.utils.constants.UserRole
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showLongToast
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityMainBinding
import com.project.acehotel.features.dashboard.booking.choose_booking.ChooseBookingActivity
import com.project.acehotel.features.dashboard.management.inventory.choose_item.ChooseItemInventoryActivity
import com.project.acehotel.features.dashboard.management.visitor.choose.ChooseVisitorActivity
import com.project.acehotel.features.popup.checkout.CheckoutDialog
import com.project.acehotel.features.popup.choose_hotel.ChooseHotelDialog
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    private var fabMenuState: FabMenuState = FabMenuState.COLLAPSED

    private var countNotCheckout: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validateToken()

        setupBottomNavbar()

        setupActionBar()

        checkNotificationPermission()

        checkNotCheckout()

        checkUserRole()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkUserRole() {
        mainViewModel.getUser().observe(this) { user ->
            user.user?.role?.let { handleFab(it) }
        }
    }

    private fun checkNotCheckout() {
        val filterDate = DateUtils.getDateThisMonth()

        mainViewModel.executeGetListBookingByHotel(filterDate).observe(this) { booking ->
            when (booking) {
                is Resource.Error -> {
                    if (!isInternetAvailable(this@MainActivity)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        Timber.tag("MainActivity").e(booking.message)
                    }
                }

                is Resource.Loading -> {

                }

                is Resource.Message -> {
                    Timber.tag("MainActivity").d(booking.message)
                }

                is Resource.Success -> {
                    if (booking.data != null) {
                        for (item in booking.data) {
                            if ((DateUtils.isTodayDate(item.checkoutDate) ||
                                        DateUtils.isDateBeforeToday(item.checkoutDate)) &&
                                item.room.first().actualCheckout == "Empty"
                            ) {
                                ++countNotCheckout
                            }
                        }

                        if (countNotCheckout != 0) {
                            CheckoutDialog(countNotCheckout).show(
                                supportFragmentManager,
                                "Reminder Checkout Dialog"
                            )
                        }
                    }
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Izin notifikasi diberikan")
            } else {
                showLongToast("Izin tidak diberikan, ini akan mempengaruhi jalannya aplikasi")
            }
        }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun validateToken() {
        mainViewModel.getRefreshToken().observe(this) { token ->
            if (token.isEmpty() || token == "") {
                TokenExpiredDialog().show(supportFragmentManager, "Token Expired Dialog")
            }
        }

        mainViewModel.getSelectedHotelData().observe(this) { hotel ->
            if (hotel.id.isEmpty()) {
                ChooseHotelDialog().show(supportFragmentManager, "Select Hotel Dialog")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleFab(userRole: UserRole) {
        binding.fabMenu.setOnClickListener {
            onFabMenuClick(userRole)
        }

        binding.fabChangeStock.setOnClickListener {
            val intentToChooseItem = Intent(this, ChooseItemInventoryActivity::class.java)
            startActivity(intentToChooseItem)
        }

        binding.fabAddBooking.setOnClickListener {
            val intentToChooseBooking = Intent(this, ChooseVisitorActivity::class.java)
            intentToChooseBooking.putExtra(FLAG_VISITOR, MENU_BOOKING)
            startActivity(intentToChooseBooking)
        }

        binding.fabCheckin.setOnClickListener {
            if (!DateUtils.isCurrentTimeAfter(13)) {
                showToast("Checkin hanya bisa dilakukan jam 13.00 waktu setempat")
            } else {
                val intentToChooseBooking = Intent(this, ChooseBookingActivity::class.java)
                intentToChooseBooking.putExtra(FLAG_VISITOR, MENU_CHECKIN)
                startActivity(intentToChooseBooking)
            }
        }

        binding.fabCheckout.setOnClickListener {
            val intentToChooseBooking = Intent(this, ChooseBookingActivity::class.java)
            intentToChooseBooking.putExtra(FLAG_VISITOR, MENU_CHECKOUT)
            startActivity(intentToChooseBooking)
        }
    }

    private fun onFabMenuClick(userRole: UserRole) {
        fabMenuState = if (fabMenuState == FabMenuState.COLLAPSED) {
            FabMenuState.EXPANDED
        } else {
            FabMenuState.COLLAPSED
        }

        setVisibility(fabMenuState == FabMenuState.EXPANDED, userRole)
        setAnimation(fabMenuState == FabMenuState.EXPANDED, userRole)
        setClickable(fabMenuState == FabMenuState.EXPANDED, userRole)
    }

    private fun setAnimation(isClicked: Boolean, userRole: UserRole) {
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

        if (userRole == UserRole.MASTER || userRole == UserRole.FRANCHISE) {
            if (isClicked) {
                binding.fabMenu.startAnimation(rotateOpen)

                binding.fabAddBooking.startAnimation(fromBottom)
                binding.tvAddBooking.startAnimation(fromBottom)

                binding.fabCheckin.startAnimation(fromBottom)
                binding.tvCheckin.startAnimation(fromBottom)

                binding.fabCheckout.startAnimation(fromBottom)
                binding.tvCheckout.startAnimation(fromBottom)

                binding.fabChangeStock.startAnimation(fromBottom)
                binding.tvChangeStock.startAnimation(fromBottom)
            } else {
                binding.fabMenu.startAnimation(rotateClose)

                binding.fabAddBooking.startAnimation(toBottom)
                binding.tvAddBooking.startAnimation(toBottom)

                binding.fabCheckin.startAnimation(toBottom)
                binding.tvCheckin.startAnimation(toBottom)

                binding.fabCheckout.startAnimation(toBottom)
                binding.tvCheckout.startAnimation(toBottom)

                binding.fabChangeStock.startAnimation(toBottom)
                binding.tvChangeStock.startAnimation(toBottom)
            }
        } else if (userRole == UserRole.INVENTORY_STAFF) {
            if (isClicked) {
                binding.fabMenu.startAnimation(rotateOpen)

                binding.fabChangeStock.startAnimation(fromBottom)
                binding.tvChangeStock.startAnimation(fromBottom)
            } else {
                binding.fabMenu.startAnimation(rotateClose)

                binding.fabChangeStock.startAnimation(toBottom)
                binding.tvChangeStock.startAnimation(toBottom)
            }
        } else if (userRole == UserRole.RECEPTIONIST) {
            if (isClicked) {
                binding.fabMenu.startAnimation(rotateOpen)

                binding.fabAddBooking.startAnimation(fromBottom)
                binding.tvAddBooking.startAnimation(fromBottom)

                binding.fabCheckin.startAnimation(fromBottom)
                binding.tvCheckin.startAnimation(fromBottom)

                binding.fabCheckout.startAnimation(fromBottom)
                binding.tvCheckout.startAnimation(fromBottom)
            } else {
                binding.fabMenu.startAnimation(rotateClose)

                binding.fabAddBooking.startAnimation(toBottom)
                binding.tvAddBooking.startAnimation(toBottom)

                binding.fabCheckin.startAnimation(toBottom)
                binding.tvCheckin.startAnimation(toBottom)

                binding.fabCheckout.startAnimation(toBottom)
                binding.tvCheckout.startAnimation(toBottom)
            }
        }
    }

    private fun setVisibility(isClicked: Boolean, userRole: UserRole) {
        if (userRole == UserRole.MASTER || userRole == UserRole.FRANCHISE) {
            if (isClicked) {
                binding.fabAddBooking.visibility = View.VISIBLE
                binding.tvAddBooking.visibility = View.VISIBLE

                binding.fabCheckin.visibility = View.VISIBLE
                binding.tvCheckin.visibility = View.VISIBLE

                binding.fabCheckout.visibility = View.VISIBLE
                binding.tvCheckout.visibility = View.VISIBLE

                binding.fabChangeStock.visibility = View.VISIBLE
                binding.tvChangeStock.visibility = View.VISIBLE
            } else {
                binding.fabAddBooking.visibility = View.INVISIBLE
                binding.tvAddBooking.visibility = View.INVISIBLE

                binding.fabCheckin.visibility = View.INVISIBLE
                binding.tvCheckin.visibility = View.INVISIBLE

                binding.fabCheckout.visibility = View.INVISIBLE
                binding.tvCheckout.visibility = View.INVISIBLE

                binding.fabChangeStock.visibility = View.INVISIBLE
                binding.tvChangeStock.visibility = View.INVISIBLE
            }
        } else if (userRole == UserRole.INVENTORY_STAFF) {
            binding.fabAddBooking.visibility = View.GONE
            binding.tvAddBooking.visibility = View.GONE

            binding.fabCheckin.visibility = View.GONE
            binding.tvCheckin.visibility = View.GONE

            binding.fabCheckout.visibility = View.GONE
            binding.tvCheckout.visibility = View.GONE

            if (isClicked) {
                binding.fabChangeStock.visibility = View.VISIBLE
                binding.tvChangeStock.visibility = View.VISIBLE
            } else {
                binding.fabChangeStock.visibility = View.INVISIBLE
                binding.tvChangeStock.visibility = View.INVISIBLE
            }
        } else if (userRole == UserRole.RECEPTIONIST) {
            binding.fabChangeStock.visibility = View.GONE
            binding.tvChangeStock.visibility = View.GONE

            if (isClicked) {
                binding.fabAddBooking.visibility = View.VISIBLE
                binding.tvAddBooking.visibility = View.VISIBLE

                binding.fabCheckin.visibility = View.VISIBLE
                binding.tvCheckin.visibility = View.VISIBLE

                binding.fabCheckout.visibility = View.VISIBLE
                binding.tvCheckout.visibility = View.VISIBLE
            } else {
                binding.fabAddBooking.visibility = View.INVISIBLE
                binding.tvAddBooking.visibility = View.INVISIBLE

                binding.fabCheckin.visibility = View.INVISIBLE
                binding.tvCheckin.visibility = View.INVISIBLE

                binding.fabCheckout.visibility = View.INVISIBLE
                binding.tvCheckout.visibility = View.INVISIBLE
            }
        }
    }

    private fun setClickable(isClicked: Boolean, userRole: UserRole) {
        if (userRole == UserRole.MASTER || userRole == UserRole.FRANCHISE) {
            if (isClicked) {
                binding.fabAddBooking.isClickable = true
                binding.fabCheckin.isClickable = true
                binding.fabCheckout.isClickable = true
                binding.fabChangeStock.isClickable = true
            } else {
                binding.fabAddBooking.isClickable = false
                binding.fabCheckin.isClickable = false
                binding.fabCheckout.isClickable = false
                binding.fabChangeStock.isClickable = false
            }
        } else if (userRole == UserRole.INVENTORY_STAFF) {
            if (isClicked) {
                binding.fabAddBooking.isClickable = false
                binding.fabCheckin.isClickable = false
                binding.fabCheckout.isClickable = false

                binding.fabChangeStock.isClickable = true
            } else {
                binding.fabAddBooking.isClickable = false
                binding.fabCheckin.isClickable = false
                binding.fabCheckout.isClickable = false

                binding.fabChangeStock.isClickable = false
            }
        } else if (userRole == UserRole.RECEPTIONIST) {
            if (isClicked) {
                binding.fabAddBooking.isClickable = true
                binding.fabCheckin.isClickable = true
                binding.fabCheckout.isClickable = true
            } else {
                binding.fabAddBooking.isClickable = false
                binding.fabCheckin.isClickable = false
                binding.fabCheckout.isClickable = false
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun setupBottomNavbar() {
        val navView: BottomNavigationView = binding.bottomNavbar
        val navViewController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.homeFragment,
                R.id.bookingFragment,
                R.id.roomFragment,
                R.id.managementFragment,
                R.id.profileFragment,
            )
        ).build()

        setupActionBarWithNavController(navViewController, appBarConfiguration)
        navView.setupWithNavController(navViewController)
    }

    companion object {
        private const val FLAG_VISITOR = "flag_visitor"

        private const val MENU_BOOKING = "booking"
        private const val MENU_CHECKIN = "checkin"
        private const val MENU_CHECKOUT = "checkout"
    }
}