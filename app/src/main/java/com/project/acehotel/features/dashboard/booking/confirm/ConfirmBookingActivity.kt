package com.project.acehotel.features.dashboard.booking.confirm

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.AddBooking
import com.project.acehotel.core.utils.*
import com.project.acehotel.core.utils.constants.RoomType
import com.project.acehotel.core.utils.constants.mapToRoomType
import com.project.acehotel.databinding.ActivityConfirmBookingBinding
import com.project.acehotel.features.dashboard.MainActivity
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class ConfirmBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmBookingBinding

    private val confirmBookingViewModel: ConfirmBookingViewModel by viewModels()

    private var bookingData: AddBooking? = null

    private var imgUri: Uri? = null
    private var getFile: File? = null

    private var totalPrice: Int? = null

    private var flagUseDisc = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        isButtonEnabled(false)

        enableRefresh(false)

        initBookingInfo()

        handleEditText()

        fetchVisitorInfo()

        handlePickImages()

        handleButtonSave()

        validateToken()
    }

    private fun validateToken() {
        confirmBookingViewModel.getRefreshToken().observe(this) { token ->
            if (token.isEmpty() || token == "") {
                TokenExpiredDialog().show(supportFragmentManager, "Token Expired Dialog")
            }
        }
    }

    private fun handleButtonSave() {
        binding.btnSave.setOnClickListener {
            enableRefresh(true)

            isButtonEnabled(false)
            showLoading(true)

            val visitorId = bookingData?.visitorId
            val checkinDate = bookingData?.checkinDate
            val duration = bookingData?.duration
            val roomCount = bookingData?.roomCount
            val extraBed = bookingData?.extraBed
            val type = mapToRoomType(bookingData?.type!!)

            val discountCode = binding.edConfirmDiscount.text.toString()

            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                PROOF_TRANSACTION,
                "Transaction_proof_${DateUtils.getCompleteCurrentDateTime()}",
                requestImageFile
            )

            val transactionProof = listOf(imageMultipart)

            confirmBookingViewModel.executeAddBooking(
                visitorId!!,
                checkinDate!!,
                duration!!,
                roomCount!!,
                extraBed!!,
                type!!,
                discountCode,
                transactionProof
            ).observe(this) { booking ->
                when (booking) {
                    is Resource.Error -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        if (!isInternetAvailable(this@ConfirmBookingActivity)) {
                            showToast(getString(R.string.check_internet))
                        } else {
                            showToast(booking.message.toString())
                        }
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                        isButtonEnabled(false)
                    }
                    is Resource.Message -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        Timber.tag("ConfirmBookingActivity").d(booking.message)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        showToast("Booking baru telah berhasil ditambahkan")

                        val intentToMain = Intent(this, MainActivity::class.java)
                        startActivity(intentToMain)
                    }
                }
            }
        }
    }

    private fun handleUseDiscount(discountCode: String, discountAmount: Int, roomCount: Int) {
        binding.apply {
            val adapter = ArrayAdapter(
                this@ConfirmBookingActivity,
                R.layout.drop_inventory_item,
                listOf(discountCode, "Tidak Menggunakan Diskon")
            )

            binding.edConfirmDiscount.apply {
                setAdapter(adapter)

                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        binding.layoutConfirmDiscount.isHintEnabled = p0.isNullOrEmpty()

                        if (!p0.isNullOrEmpty()) {
                            if (p0.toString() == discountCode) {
                                if (!flagUseDisc) {
                                    flagUseDisc = true

                                    totalPrice = totalPrice?.minus(discountAmount * roomCount)

                                    tvConfirmDiscPrice.text = "Rp ${formatNumber(discountAmount!!)}"
                                    tvConfirmTotalPrice.text = "Rp ${formatNumber(totalPrice!!)}"
                                }
                            } else if (p0.toString() == "Tidak Menggunakan Diskon") {
                                if (!flagUseDisc) {
                                    tvConfirmDiscPrice.text = "Rp 0"
                                } else {
                                    flagUseDisc = false

                                    totalPrice = totalPrice?.plus(discountAmount * roomCount)

                                    tvConfirmDiscPrice.text = "Rp ${formatNumber(discountAmount!!)}"
                                    tvConfirmTotalPrice.text = "Rp ${formatNumber(totalPrice!!)}"
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initBookingInfo() {
        val data = intent.getStringExtra(BOOKING_DATA)
        bookingData = Gson().fromJson(data, AddBooking::class.java)

        if (bookingData != null) {
            binding.apply {
                confirmBookingViewModel.getSelectedHotelData()
                    .observe(this@ConfirmBookingActivity) { hotel ->
                        tvConfirmCheckinDate.text =
                            DateUtils.convertToDisplayDateFormat(bookingData?.checkinDate!!)
                        tvConfirmCheckoutDate.text = DateUtils.convertToDisplayDateFormat(
                            DateUtils.calculateCheckoutDate(
                                bookingData?.checkinDate!!,
                                bookingData?.duration!!.toLong()
                            )
                        )
                        tvConfirmNightCount.text = "${bookingData?.duration} malam"
                        tvConfirmRoomBook.text = bookingData?.type

                        tvConfirmBedPrice.text =
                            "Rp ${formatNumber(hotel.extraBedPrice * bookingData!!.extraBed)}"
                        tvConfirmBedDesc.text = "${bookingData!!.extraBed} unit"

                        tvConfirmRoomDesc.text =
                            "${bookingData!!.duration} malam x ${bookingData!!.roomCount} kamar"

                        tvConfirmDiscPrice.text = "Rp 0"

                        if (bookingData!!.type == RoomType.REGULAR.display) {
                            tvConfirmRoom.text = "Kamar ${RoomType.REGULAR.display}"

                            tvConfirmRoomPrice.text =
                                "Rp ${formatNumber(hotel.regularRoomPrice * bookingData!!.roomCount)}"

                            totalPrice =
                                bookingData!!.extraBed * hotel.extraBedPrice + bookingData!!.roomCount * hotel.regularRoomPrice
                            tvConfirmTotalPrice.text = "Rp ${formatNumber(totalPrice!!)}"

                            handleUseDiscount(
                                hotel.discount,
                                hotel.discountAmount,
                                bookingData!!.roomCount
                            )
                        } else {
                            tvConfirmRoom.text = "Kamar ${RoomType.EXCLUSIVE.display}"

                            tvConfirmRoomPrice.text =
                                "Rp ${formatNumber(hotel.exclusiveRoomPrice * bookingData!!.roomCount)}"

                            totalPrice =
                                bookingData!!.extraBed * hotel.extraBedPrice + bookingData!!.roomCount * hotel.exclusiveRoomPrice
                            tvConfirmTotalPrice.text = "Rp ${formatNumber(totalPrice!!)}"

                            handleUseDiscount(
                                hotel.discount,
                                hotel.discountAmount,
                                bookingData!!.roomCount
                            )
                        }
                    }
            }
        }
    }

    private fun fetchVisitorInfo() {
        if (bookingData != null) {
            confirmBookingViewModel.getVisitorDetail(bookingData?.visitorId!!)
                .observe(this) { visitor ->
                    when (visitor) {
                        is Resource.Error -> {
                            showLoading(false)

                            if (!isInternetAvailable(this@ConfirmBookingActivity)) {
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

                            Timber.tag("ConfirmBookingActivity").d(visitor.message)
                        }
                        is Resource.Success -> {
                            showLoading(false)

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

    private fun checkForms() {
        binding.apply {
            val disc = edConfirmDiscount.text.toString()

            isButtonEnabled(
                disc.isNotEmpty() &&
                        getFile != null
            )
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
        binding.edConfirmDiscount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkForms()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.isNullOrEmpty()) {
                    checkForms()
                    binding.layoutConfirmDiscount.error = getString(R.string.field_cant_empty)
                } else {
                    binding.layoutConfirmDiscount.error = null
                }
            }
        })
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

    private fun enableRefresh(isDisable: Boolean) {
        binding.refConfirm.isEnabled = isDisable
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }


    companion object {
        private const val BOOKING_DATA = "booking_data"
        private const val PROOF_TRANSACTION = "image"
    }
}