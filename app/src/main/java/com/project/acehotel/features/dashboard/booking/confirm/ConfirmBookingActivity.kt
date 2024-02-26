package com.project.acehotel.features.dashboard.booking.confirm

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.AddBooking
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.uriToFile
import com.project.acehotel.databinding.ActivityConfirmBookingBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class ConfirmBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmBookingBinding

    private val confirmBookingViewModel: ConfirmBookingViewModel by viewModels()

    private var bookingData: AddBooking? = null

    private var imgUri: Uri? = null
    private var getFile: File? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        isButtonEnabled(false)

        disableRefresh()

        initBookingInfo()

        handleEditText()

        fetchVisitorInfo()

        handlePickImages()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initBookingInfo() {
        val data = intent.getStringExtra(BOOKING_DATA)
        bookingData = Gson().fromJson(data, AddBooking::class.java)

        if (bookingData != null) {
            binding.apply {
                tvConfirmCheckinDate.text = bookingData?.checkinDate
                tvConfirmCheckoutDate.text = DateUtils.calculateCheckoutDate(
                    bookingData?.checkinDate!!,
                    bookingData?.duration!!.toLong()
                )
                tvConfirmNightCount.text = "${bookingData?.duration} malam"
                tvConfirmRoomBook.text = bookingData?.type


            }
        }
    }

    private fun fetchVisitorInfo() {
        if (bookingData != null) {
            confirmBookingViewModel.getVisitorDetail(bookingData?.visitorId!!)
                .observe(this) { visitor ->
                    when (visitor) {
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Message -> {

                        }
                        is Resource.Success -> {
                            binding.apply {
                                tvVisitorDetailName.text = visitor.data?.name
                                tvVisitorDetailNik.text = visitor.data?.identity_num
                                tvVisitorDetailPhone.text = visitor.data?.phone
                                tvVisitorDetailEmail.text = visitor.data?.email

                                Glide.with(this@ConfirmBookingActivity)
                                    .load(visitor.data?.identityImage).into(ivConfirmVisitor)
                            }
                        }
                    }
                }
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imgUri = uri
            binding.ivConfirmPayment.setImageURI(imgUri)

            getFile = uriToFile(imgUri!!, this)

            checkForms()
        } else {
            Timber.tag("Photo Picker").d("No media selected")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun handlePickImages() {
        binding.ivConfirmPayment.setOnClickListener {
            startGallery()
        }
    }

    private fun handleEditText() {

    }

    private fun checkForms() {

    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnSave.isEnabled = isEnabled
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
    }
}