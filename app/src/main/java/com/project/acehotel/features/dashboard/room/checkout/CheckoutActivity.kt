package com.project.acehotel.features.dashboard.room.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.CurrentVisitorStatus
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityCheckoutBinding
import com.project.acehotel.features.dashboard.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private val checkoutViewModel: CheckoutViewModel by viewModels()

    private var bookingData: Booking? = null

    private var cbBedValue = false
    private var cbBedBlackValue = false
    private var cbTvValue = false
    private var cbAcRemoteValue = false
    private var cbHangerValue = false
    private var cbCarpetValue = false
    private var cbMirrorValue = false
    private var cbSelendangValue = false
    private var cbTrashValue = false
    private var cbChairValue = false
    private var cbShowerValue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        isButtonEnabled(false)

        fetchBookingInfo()

        handleProblemCheckbox()

        handleEditText()

        handleSaveButton()

        enableRefresh(false)
    }

    private fun handleEditText() {
        binding.apply {
            cbBed.setOnCheckedChangeListener { _, isChecked ->
                cbBedValue = isChecked
                checkForms()
            }

            cbBedBlack.setOnCheckedChangeListener { _, isChecked ->
                cbBedBlackValue = isChecked
                checkForms()
            }

            cbTv.setOnCheckedChangeListener { _, isChecked ->
                cbTvValue = isChecked
                checkForms()
            }

            cbAcRemote.setOnCheckedChangeListener { _, isChecked ->
                cbAcRemoteValue = isChecked
                checkForms()
            }

            cbHanger.setOnCheckedChangeListener { _, isChecked ->
                cbHangerValue = isChecked
                checkForms()
            }

            cbCarpet.setOnCheckedChangeListener { _, isChecked ->
                cbCarpetValue = isChecked
                checkForms()
            }

            cbMirror.setOnCheckedChangeListener { _, isChecked ->
                cbMirrorValue = isChecked
                checkForms()
            }

            cbSelendang.setOnCheckedChangeListener { _, isChecked ->
                cbSelendangValue = isChecked
                checkForms()
            }

            cbTrash.setOnCheckedChangeListener { _, isChecked ->
                cbTrashValue = isChecked
                checkForms()
            }

            cbChair.setOnCheckedChangeListener { _, isChecked ->
                cbChairValue = isChecked
                checkForms()
            }

            cbShower.setOnCheckedChangeListener { _, isChecked ->
                cbShowerValue = isChecked
                checkForms()
            }
        }
    }

    private fun checkForms() {
        isButtonEnabled(
            cbBedValue && cbBedBlackValue && cbTvValue && cbAcRemoteValue && cbHangerValue && cbCarpetValue && cbMirrorValue && cbShowerValue && cbTrashValue && cbChairValue
        )
    }

    private fun handleSaveButton() {
        binding.btnCheckout.setOnClickListener {
            enableRefresh(true)
            isButtonEnabled(false)

            val bookingData =
                Gson().fromJson(intent.getStringExtra(BOOKING_DATA), Booking::class.java)
            val note = binding.edCheckoutNote.text.toString().ifEmpty { "" }

            checkoutViewModel.roomCheckout(
                bookingData.room.first().id,
                DateUtils.getDateThisDay(),
                bookingData.id,
                bookingData.visitorId,

                cbBedValue,
                cbBedBlackValue,
                cbTvValue,
                cbTvValue,
                cbAcRemoteValue,
                cbHangerValue,
                cbCarpetValue,
                cbMirrorValue,
                cbShowerValue,
                cbSelendangValue,
                cbTrashValue,
                cbChairValue,

                note
            ).observe(this) { room ->
                when (room) {
                    is Resource.Error -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        if (!isInternetAvailable(this@CheckoutActivity)) {
                            showToast(getString(R.string.check_internet))
                        } else {
                            showToast(room.message.toString())
                        }
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                        isButtonEnabled(false)
                    }
                    is Resource.Message -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        Timber.tag("CheckoutActivity").d(room.message)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        showToast("Pengunjung telah berhasil checkin")

                        val intentToMain = Intent(this@CheckoutActivity, MainActivity::class.java)
                        startActivity(intentToMain)
                        finish()
                    }
                }
            }
        }
    }

    private fun handleProblemCheckbox() {
        binding.apply {
            cbHasProblem.setOnClickListener {
                if (cbHasProblem.isChecked) {
                    layoutCheckoutNote.visibility = View.VISIBLE
                    edCheckoutNote.visibility = View.VISIBLE
                } else {
                    layoutCheckoutNote.visibility = View.GONE
                    edCheckoutNote.visibility = View.GONE
                }
            }
        }
    }

    private fun fetchBookingInfo() {
        val data = intent.getStringExtra(BOOKING_DATA)
        bookingData = Gson().fromJson(data, Booking::class.java)

        if (bookingData != null) {
            binding.apply {
                customCardVisitor.setVisitorDate(
                    "${DateUtils.convertDate(bookingData!!.checkinDate)} - ${
                        DateUtils.convertDate(
                            bookingData!!.checkoutDate
                        )
                    }"
                )
                customCardVisitor.setVisitorName(bookingData!!.visitorName)
                customCardVisitor.setVisitorStatus(CurrentVisitorStatus.CHECKIN.status, "", false)

                edCheckoutTime.setText(DateUtils.getCurrentDateTime())
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refCheckout.isRefreshing = isLoading
    }

    private fun enableRefresh(isEnabled: Boolean) {
        binding.refCheckout.isEnabled = isEnabled
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnCheckout.isEnabled = isEnabled
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val BOOKING_DATA = "booking_data"
    }
}