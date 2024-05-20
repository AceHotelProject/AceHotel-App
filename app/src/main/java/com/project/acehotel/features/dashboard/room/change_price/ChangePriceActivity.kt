package com.project.acehotel.features.dashboard.room.change_price

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityChangePriceBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChangePriceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePriceBinding

    private val changePriceViewModel: ChangePriceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePriceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        isButtonEnabled(false)

        handleButtonBack()

        fetchPriceInfo()

        handleEditText()

        enableRefresh(false)

        handleSaveButton()
    }

    private fun handleEditText() {
        binding.apply {
            edChangePriceRegular.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutChangePriceRegular.error =
                            getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutChangePriceRegular.error = null
                    }
                }
            })

            edChangePriceExclusive.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutChangePriceExclusive.error =
                            getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutChangePriceExclusive.error = null
                    }
                }
            })

            edChangePriceBedPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutChangePriceBedPrice.error =
                            getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutChangePriceBedPrice.error = null
                    }
                }
            })

            edChangePriceDiscount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edChangePriceDiscountPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })
        }
    }

    private fun handleSaveButton() {
        binding.apply {
            btnSave.setOnClickListener {
                val bedPrice = if (edChangePriceBedPrice.text.toString()
                        .isNotEmpty()
                ) edChangePriceBedPrice.text.toString().toInt() else 0

                val regulerPrice = if (edChangePriceRegular.text.toString()
                        .isNotEmpty()
                ) edChangePriceRegular.text.toString().toInt() else 0

                val exclusivePrice = if (edChangePriceExclusive.text.toString()
                        .isNotEmpty()
                ) edChangePriceExclusive.text.toString().toInt() else 0

                val discountCode = edChangePriceDiscount.text.toString().ifEmpty { "Empty" }

                val discountPrice = if (edChangePriceDiscountPrice.text.toString()
                        .isNotEmpty()
                ) edChangePriceDiscountPrice.text.toString().toInt() else 0

                Timber.tag("BED").e(bedPrice.toString())
                Timber.tag("DISC").e(discountPrice.toString())

                changePriceViewModel.executeUpdateHotelPrice(
                    discountCode,
                    discountPrice,
                    regulerPrice,
                    exclusivePrice,
                    bedPrice
                ).observe(this@ChangePriceActivity) { price ->
                    when (price) {
                        is Resource.Error -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            if (!isInternetAvailable(this@ChangePriceActivity)) {
                                showToast(getString(R.string.check_internet))
                            } else {
                                showToast(price.message.toString())
                            }
                        }

                        is Resource.Loading -> {
                            showLoading(true)
                            isButtonEnabled(false)
                        }

                        is Resource.Message -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            Timber.tag("AddItemInventoryActivity").d(price.message)
                        }

                        is Resource.Success -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            if (price.data?.discountCode?.isNotEmpty()!! && price.data.discountAmount != 0) {
                                Timber.tag("BED").e("Bos" + price.data.extraBedPrice.toString())
                                Timber.tag("DISC").e("Bos" + price.data.discountAmount.toString())

                                changePriceViewModel.saveSelectedHotelData(
                                    ManageHotel(
                                        id = price.data.id,

                                        name = price.data.name,
                                        address = price.data.address,
                                        contact = price.data.contact,

                                        regularRoomCount = price.data.regularRoomCount,
                                        regularRoomPrice = price.data.regularRoomPrice,

                                        exclusiveRoomCount = price.data.exclusiveRoomCount,
                                        exclusiveRoomPrice = price.data.exclusiveRoomPrice,

                                        extraBedPrice = price.data.extraBedPrice,

                                        discount = price.data.discountCode,
                                        discountAmount = price.data.discountAmount,

                                        roomId = price.data.roomId.map { room ->
                                            room.id
                                        },
                                        inventoryId = price.data.inventoryId,

                                        ownerId = price.data.owner.id.toString(),
                                        ownerName = price.data.owner.username.toString(),
                                        ownerEmail = price.data.owner.email.toString(),

                                        receptionistId = price.data.receptionist.id.toString(),
                                        receptionistName = price.data.receptionist.username.toString(),
                                        receptionistEmail = price.data.receptionist.email.toString(),

                                        cleaningStaffId = price.data.cleaningStaff.id.toString(),
                                        cleaningStaffEmail = price.data.cleaningStaff.email.toString(),
                                        cleaningStaffName = price.data.cleaningStaff.username.toString(),

                                        inventoryStaffId = price.data.inventoryStaff.id.toString(),
                                        inventoryStaffName = price.data.inventoryStaff.username.toString(),
                                        inventoryStaffEmail = price.data.inventoryStaff.email.toString()
                                    )
                                ).observe(this@ChangePriceActivity) { hotel ->
                                    if (hotel) {
                                        showToast("List harga berhasil diperbaharui")
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkForms() {
        binding.apply {
            val bedPrice = edChangePriceBedPrice.text.toString()
            val regulerPrice = edChangePriceRegular.text.toString()
            val exclusivePrice = edChangePriceExclusive.text.toString()
            val discountCode = edChangePriceDiscount.text.toString()
            val discountPrice = edChangePriceDiscountPrice.text.toString()

            isButtonEnabled(
                bedPrice.isNotEmpty() &&
                        regulerPrice.isNotEmpty() &&
                        exclusivePrice.isNotEmpty()
            )

            if (discountPrice.isNotEmpty() || discountCode.isNotEmpty()) {
                isButtonEnabled(
                    bedPrice.isNotEmpty() &&
                            regulerPrice.isNotEmpty() &&
                            exclusivePrice.isNotEmpty() &&
                            discountPrice.isNotEmpty() &&
                            discountCode.isNotEmpty()
                )
            }
        }
    }

    private fun fetchPriceInfo() {
        changePriceViewModel.getSelectedHotelData().observe(this) { hotel ->
            binding.apply {
                edChangePriceBedPrice.setText(hotel.extraBedPrice.toString())
                edChangePriceExclusive.setText(hotel.exclusiveRoomPrice.toString())
                edChangePriceRegular.setText(hotel.regularRoomPrice.toString())

                if (hotel.discount.isNotEmpty() && hotel.discount != "Empty") {
                    edChangePriceDiscount.setText(hotel.discount)
                    Timber.tag("CHANGE").e(hotel.discountAmount.toString())
                    edChangePriceDiscountPrice.setText(hotel.discountAmount.toString())
                }
            }
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
        binding.refChangePrice.isEnabled = isDisable
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refChangePrice.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}