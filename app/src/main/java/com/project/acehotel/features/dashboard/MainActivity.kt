package com.project.acehotel.features.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.acehotel.R
import com.project.acehotel.core.utils.constants.FabMenuState
import com.project.acehotel.databinding.ActivityMainBinding
import com.project.acehotel.features.dashboard.management.inventory.choose_item.ChooseItemInventoryActivity
import com.project.acehotel.features.dashboard.management.visitor.choose.ChooseVisitorActivity
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    private var fabMenuState: FabMenuState = FabMenuState.COLLAPSED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavbar()

        setupActionBar()

        handleFab()

        validateToken()
    }

    private fun validateToken() {
        mainViewModel.getRefreshToken().observe(this) { token ->
            if (token.isNullOrEmpty()) {
                TokenExpiredDialog().show(supportFragmentManager, "Token Expired Dialog")
            }
        }
    }

    private fun handleFab() {
        binding.fabMenu.setOnClickListener {
            onFabMenuClick()
        }

        binding.fabChangeStock.setOnClickListener {
            val intentToChooseItem = Intent(this, ChooseItemInventoryActivity::class.java)
            startActivity(intentToChooseItem)
        }

        binding.fabAddBooking.setOnClickListener {
            val intentToChooseVisitor = Intent(this, ChooseVisitorActivity::class.java)
            startActivity(intentToChooseVisitor)
        }

        binding.fabCheckin.setOnClickListener {
            val intentToChooseVisitor = Intent(this, ChooseVisitorActivity::class.java)
            startActivity(intentToChooseVisitor)
        }

        binding.fabCheckout.setOnClickListener {
            val intentToChooseVisitor = Intent(this, ChooseVisitorActivity::class.java)
            startActivity(intentToChooseVisitor)
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
    }

    private fun setVisibility(isClicked: Boolean) {
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
    }

    private fun setClickable(isClicked: Boolean) {
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


}