package com.project.acehotel.features.dashboard.booking.add_booking

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.domain.booking.model.AddBooking
import com.project.acehotel.core.utils.constants.roomTypeList
import com.project.acehotel.databinding.ActivityAddBookingBinding
import com.project.acehotel.features.dashboard.booking.confirm.ConfirmBookingActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookingBinding

    private val addBookingViewModel: AddBookingViewModel by viewModels()

    private var savedDate: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        isButtonEnabled(false)

        initFormData()

        enableRefresh(false)

        handleEditText()

        initVisitorInfo()

        initRoomType()

        handlePickDate()

        handleSaveButton()
    }

    private fun initFormData() {
        binding.apply {
            //for now we lock to only 1 room
            //to add more room just create another booking
            edAddBookingBedCount.setText("0")
            edAddBookingRoomCount.setText("1")
            isEditTextEditable(edAddBookingRoomCount, false)
        }
    }

    private fun isEditTextEditable(editText: TextInputEditText, isEditable: Boolean) {
        editText.isFocusable = isEditable
        editText.isClickable = isEditable
        editText.isFocusableInTouchMode = isEditable
        editText.isCursorVisible = isEditable
    }

    private fun handleSaveButton() {
        binding.apply {
            btnSave.setOnClickListener {
                var hotelId = ""
                addBookingViewModel.getSelectedHotelData().observe(this@AddBookingActivity) {
                    hotelId = it.id
                }

                val visitorId = intent.getStringExtra(VISITOR_ID) ?: "Empty"
                val checkinDate = savedDate
                val duration = edAddBookingNightCount.text.toString().toInt()
                val roomCount = edAddBookingRoomCount.text.toString().toInt()
                val extraBed = edAddBookingBedCount.text.toString().toInt()
                val type = edAddBookingRoomType.text.toString()

                val data = AddBooking(
                    hotelId,
                    visitorId,
                    checkinDate,
                    type,
                    duration,
                    roomCount,
                    extraBed
                )
                val dataToJson = Gson().toJson(data)

                val intentToConfirmBooking =
                    Intent(this@AddBookingActivity, ConfirmBookingActivity::class.java)

                intentToConfirmBooking.putExtra(BOOKING_DATA, dataToJson)
                startActivity(intentToConfirmBooking)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handlePickDate() {
        binding.edAddBookingCheckin.setOnClickListener {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker =
                DatePickerDialog(
                    this@AddBookingActivity, { view, year, monthOfYear, dayOfMonth ->
                        savedDate = "$year-${monthOfYear + 1}-$dayOfMonth"

                        binding.edAddBookingCheckin.setText("$dayOfMonth-${monthOfYear + 1}-$year")
                    },
                    year,
                    month,
                    day
                )

            datePicker.show()
        }
    }

    private fun initRoomType() {
        val adapter = ArrayAdapter(this, R.layout.drop_inventory_item, roomTypeList)

        binding.edAddBookingRoomType.apply {
            setAdapter(adapter)

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    binding.layoutAddBookingRoomType.isHintEnabled = p0.isNullOrEmpty()
                }
            })
        }
    }

    private fun initVisitorInfo() {
        var name = intent.getStringExtra(VISITOR_NAME)

        binding.tvVisitorCardName.text = name
    }

    private fun handleEditText() {
        binding.apply {
            edAddBookingCheckin.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutAddBookingCheckin.error = getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutAddBookingCheckin.error = null
                    }
                }
            })

            edAddBookingNightCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutAddBookingNightCount.error =
                            getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutAddBookingNightCount.error = null
                    }
                }
            })

            edAddBookingRoomCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutAddBookingRoomCount.error =
                            getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutAddBookingRoomCount.error = null
                    }
                }
            })

            edAddBookingRoomType.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutAddBookingRoomType.error =
                            getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutAddBookingRoomType.error = null
                    }
                }
            })

            edAddBookingBedCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutAddBookingBedCount.error =
                            getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutAddBookingBedCount.error = null
                    }
                }
            })
        }
    }

    private fun checkForms() {
        binding.apply {
            val id = intent.getStringExtra(VISITOR_ID)
            val checkinDate = savedDate
            val duration =
                if (edAddBookingNightCount.text.toString() == "") 0 else edAddBookingNightCount.text.toString()
                    .toInt()
            val roomCount =
                if (edAddBookingRoomCount.text.toString() == "") 0 else edAddBookingRoomCount.text.toString()
                    .toInt()
            val extraBed =
                if (edAddBookingBedCount.text.toString() == "") 0 else edAddBookingBedCount.text.toString()
                    .toInt()
            val type = edAddBookingRoomType.text.toString()

            isButtonEnabled(
                id != null &&
                        checkinDate.isNotEmpty() &&
                        duration != 0 &&
                        roomCount != 0 &&
                        extraBed >= 0 &&
                        type.isNotEmpty()
            )
        }
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnSave.isEnabled = isEnabled
    }

    private fun enableRefresh(isDisable: Boolean) {
        binding.refAddBooking.isEnabled = isDisable
    }

    override fun onResume() {
        super.onResume()

        checkForms()
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val VISITOR_NAME = "name_visitor"
        private const val VISITOR_ID = "name_id"

        private const val BOOKING_DATA = "booking_data"
    }
}