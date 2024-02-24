package com.project.acehotel.features.dashboard.profile.add_franchise

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.*
import com.project.acehotel.core.utils.constants.DeleteDialogType
import com.project.acehotel.databinding.ActivityAddFranchiseBinding
import com.project.acehotel.features.dashboard.profile.manage_franchise.ManageFranchiseActivity
import com.project.acehotel.features.popup.delete.DeleteItemDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class AddFranchiseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFranchiseBinding
    private val addFranchiseViewModel: AddFranchiseViewModel by viewModels()

    private var flagImgReg = 0
    private var flagImgExc = 0

    private var reg1ImgUri: Uri? = null
    private var exc1ImgUri: Uri? = null

    private var getFileRegular1: File? = null
    private var getFileExclusive1: File? = null

    private var savedRegularRoomImage: String = ""
    private var savedExclusiveRoomImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isButtonEnabled(false)

        setupActionBar()

        handleButtonBack()

        handleEditText()

        handlePickImages()

        setupUI()
    }

    private fun setupUI() {
        val hotelId = intent.getStringExtra(HOTEL_ID) ?: ""

        when (intent.getStringExtra(FLAG_HOTEL_UI) ?: "") {
            FLAG_HOTEL_DETAIL -> {
                binding.apply {
                    binding.tvTitle.text = "Detail Hotel"

                    fetchHotelInfo(hotelId)

                    isEditable(false)

                    setupButtonMore(hotelId)
                }
            }
            FLAG_HOTEL_UPDATE -> {
                binding.apply {
                    binding.tvTitle.text = "Perbaharui Hotel"

                    fetchHotelInfo(hotelId)

                    isEditable(true)

                    handleUpdateHotelSaveButton(hotelId)
                }
            }
            else -> {
                binding.tvTitle.text = "Tambah Hotel"


                handleAddHotelSaveButton()
            }
        }
    }

    private fun handleUpdateHotelSaveButton(hotelId: String) {
        binding.apply {
            btnSave.setOnClickListener {
                isButtonEnabled(false)
                showLoading(true)

                val hotelName = edAddFranchiseName.text.toString()
                val hotelAddress = edAddFranchiseAddress.text.toString()
                val hotelContact = edAddFranchiseContact.text.toString()

                val regularCount =
                    if (edAddFranchiseRoomRegularCount.text.toString() == "") 0
                    else edAddFranchiseRoomRegularCount.text.toString().toInt()
                val regularPrice =
                    if (edAddFranchiseRoomRegularPrice.text.toString() == "") 0
                    else edAddFranchiseRoomRegularPrice.text.toString().toInt()
                val exclusiveCount =
                    if (edAddFranchiseRoomExclusiveCount.text.toString() == "") 0
                    else edAddFranchiseRoomExclusiveCount.text.toString().toInt()
                val exclusivePrice =
                    if (edAddFranchiseRoomExclusivePrice.text.toString() == "") 0
                    else edAddFranchiseRoomExclusivePrice.text.toString().toInt()
                val bedPrice =
                    if (edAddFranchiseRoomBedPrice.text.toString() == "") 0
                    else edAddFranchiseRoomBedPrice.text.toString().toInt()

                if (getFileExclusive1 != null || getFileRegular1 != null) {
                    if (getFileExclusive1 != null && getFileRegular1 != null) {
                        Timber.tag("TEST").e("KEDUA IMAGE DIUBAH")

                        val fileExclusive1 = reduceFileImage(getFileExclusive1 as File)
                        val requestImageFileExclusive1 =
                            fileExclusive1.asRequestBody("image/jpeg".toMediaType())
                        val imageMultipartExclusive1: MultipartBody.Part =
                            MultipartBody.Part.createFormData(
                                EXCLUSIVE_PHOTO,
                                "Exclusive_Room_Photo_${DateUtils.getCompleteCurentDateTime()}",
                                requestImageFileExclusive1
                            )

                        val fileRegular1 = reduceFileImage(getFileRegular1 as File)
                        val requestImageFileRegular1 =
                            fileRegular1.asRequestBody("image/jpeg".toMediaType())
                        val imageMultipartRegular1: MultipartBody.Part =
                            MultipartBody.Part.createFormData(
                                REGULAR_PHOTO,
                                "Regular_Room_Photo_${DateUtils.getCompleteCurentDateTime()}",
                                requestImageFileRegular1
                            )

                        val listOfRoomImage =
                            listOf(
                                imageMultipartRegular1,
                                imageMultipartExclusive1
                            )
                        addFranchiseViewModel.executeUpdateHotel(
                            listOfRoomImage,
                            hotelId,
                            hotelName,
                            hotelAddress,
                            hotelContact,
                            regularCount,
                            null,
                            exclusiveCount,
                            null,
                            regularPrice,
                            exclusivePrice,
                            bedPrice,
                            isRegularImageChanged = true,
                            isExclusiveImageChanged = true
                        ).observe(this@AddFranchiseActivity) { hotel ->
                            when (hotel) {
                                is Resource.Error -> {
                                    showLoading(false)

                                    if (!isInternetAvailable(this@AddFranchiseActivity)) {
                                        showToast(getString(R.string.check_internet))
                                    } else {
                                        showToast(hotel.message.toString())
                                    }

                                    isButtonEnabled(true)
                                }
                                is Resource.Loading -> {
                                    showLoading(true)
                                    isButtonEnabled(false)
                                }
                                is Resource.Message -> {
                                    showLoading(false)
                                    isButtonEnabled(true)
                                }
                                is Resource.Success -> {
                                    showLoading(false)
                                    isButtonEnabled(true)

                                    showToast("Data hotel berhasil diperbaharui")
                                    val intentToManageFranchise = Intent(
                                        this@AddFranchiseActivity,
                                        ManageFranchiseActivity::class.java
                                    )
                                    startActivity(intentToManageFranchise)
                                    finish()
                                }
                            }
                        }
                    } else if (getFileRegular1 != null) {
                        Timber.tag("TEST").e("IMAGE REGULAR DIUBAH")

                        val fileRegular1 = reduceFileImage(getFileRegular1 as File)
                        val requestImageFileRegular1 =
                            fileRegular1.asRequestBody("image/jpeg".toMediaType())
                        val imageMultipartRegular1: MultipartBody.Part =
                            MultipartBody.Part.createFormData(
                                REGULAR_PHOTO,
                                "Regular_Room_Photo_${DateUtils.getCompleteCurentDateTime()}",
                                requestImageFileRegular1
                            )

                        val listOfRoomImage =
                            listOf(
                                imageMultipartRegular1,
                            )

                        addFranchiseViewModel.executeUpdateHotel(
                            listOfRoomImage,
                            hotelId,
                            hotelName,
                            hotelAddress,
                            hotelContact,
                            regularCount,
                            null,
                            exclusiveCount,
                            savedExclusiveRoomImage,
                            regularPrice,
                            exclusivePrice,
                            bedPrice,
                            isRegularImageChanged = true,
                            isExclusiveImageChanged = false
                        ).observe(this@AddFranchiseActivity) { hotel ->
                            when (hotel) {
                                is Resource.Error -> {
                                    showLoading(false)

                                    if (!isInternetAvailable(this@AddFranchiseActivity)) {
                                        showToast(getString(R.string.check_internet))
                                    } else {
                                        showToast(hotel.message.toString())
                                    }

                                    isButtonEnabled(true)
                                }
                                is Resource.Loading -> {
                                    showLoading(true)
                                    isButtonEnabled(false)
                                }
                                is Resource.Message -> {
                                    showLoading(false)
                                    isButtonEnabled(true)
                                }
                                is Resource.Success -> {
                                    showLoading(false)
                                    isButtonEnabled(true)

                                    showToast("Data hotel berhasil diperbaharui")
                                    val intentToManageFranchise = Intent(
                                        this@AddFranchiseActivity,
                                        ManageFranchiseActivity::class.java
                                    )
                                    startActivity(intentToManageFranchise)
                                    finish()
                                }
                            }
                        }
                    } else {
                        Timber.tag("TEST").e("IMAGE EXCLUSIVE DIUBAH")

                        val fileExclusive1 = reduceFileImage(getFileExclusive1 as File)
                        val requestImageFileExclusive1 =
                            fileExclusive1.asRequestBody("image/jpeg".toMediaType())
                        val imageMultipartExclusive1: MultipartBody.Part =
                            MultipartBody.Part.createFormData(
                                EXCLUSIVE_PHOTO,
                                "Exclusive_Room_Photo_${DateUtils.getCompleteCurentDateTime()}",
                                requestImageFileExclusive1
                            )

                        val listOfRoomImage =
                            listOf(
                                imageMultipartExclusive1
                            )

                        addFranchiseViewModel.executeUpdateHotel(
                            listOfRoomImage,
                            hotelId,
                            hotelName,
                            hotelAddress,
                            hotelContact,
                            regularCount,
                            savedRegularRoomImage,
                            exclusiveCount,
                            null,
                            regularPrice,
                            exclusivePrice,
                            bedPrice,
                            isRegularImageChanged = false,
                            isExclusiveImageChanged = true
                        ).observe(this@AddFranchiseActivity) { hotel ->
                            when (hotel) {
                                is Resource.Error -> {
                                    showLoading(false)

                                    if (!isInternetAvailable(this@AddFranchiseActivity)) {
                                        showToast(getString(R.string.check_internet))
                                    } else {
                                        showToast(hotel.message.toString())
                                    }

                                    isButtonEnabled(true)
                                }
                                is Resource.Loading -> {
                                    showLoading(true)
                                    isButtonEnabled(false)
                                }
                                is Resource.Message -> {
                                    showLoading(false)
                                    isButtonEnabled(true)
                                }
                                is Resource.Success -> {
                                    showLoading(false)
                                    isButtonEnabled(true)

                                    showToast("Data hotel berhasil diperbaharui")
                                    val intentToManageFranchise = Intent(
                                        this@AddFranchiseActivity,
                                        ManageFranchiseActivity::class.java
                                    )
                                    startActivity(intentToManageFranchise)
                                    finish()
                                }
                            }
                        }
                    }
                } else {
                    addFranchiseViewModel.updateHotel(
                        hotelId, hotelName,
                        hotelAddress,
                        hotelContact,
                        regularCount,
                        savedExclusiveRoomImage,
                        exclusiveCount,
                        savedExclusiveRoomImage,
                        regularPrice,
                        exclusivePrice,
                        bedPrice,
                    ).observe(this@AddFranchiseActivity) { hotel ->
                        when (hotel) {
                            is Resource.Error -> {
                                showLoading(false)

                                if (!isInternetAvailable(this@AddFranchiseActivity)) {
                                    showToast(getString(R.string.check_internet))
                                } else {
                                    showToast(hotel.message.toString())
                                }

                                isButtonEnabled(true)
                            }
                            is Resource.Loading -> {
                                showLoading(true)
                                isButtonEnabled(false)
                            }
                            is Resource.Message -> {
                                showLoading(false)
                                isButtonEnabled(true)
                            }
                            is Resource.Success -> {
                                showLoading(false)
                                isButtonEnabled(true)

                                showToast("Data hotel berhasil diperbaharui")
                                val intentToManageFranchise = Intent(
                                    this@AddFranchiseActivity,
                                    ManageFranchiseActivity::class.java
                                )
                                startActivity(intentToManageFranchise)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isEditable(editable: Boolean) {
        binding.apply {
            if (editable) {
                btnFranchiseDetailMore.visibility = View.INVISIBLE
                constraintLayout5.visibility = View.VISIBLE

                isEditTextEditable(edAddFranchiseName, editable)
                isEditTextEditable(edAddFranchiseAddress, editable)
                isEditTextEditable(edAddFranchiseContact, editable)

                isEditTextEditable(edAddFranchiseRoomRegularCount, editable)
                isEditTextEditable(edAddFranchiseRoomRegularPrice, editable)
                isEditTextEditable(edAddFranchiseRoomExclusiveCount, editable)
                isEditTextEditable(edAddFranchiseRoomExclusivePrice, editable)
                isEditTextEditable(edAddFranchiseRoomBedPrice, editable)

                isEditTextEditable(edAddFranchiseOwnerName, editable)
                isEditTextEditable(edAddFranchiseOwnerEmail, editable)
                isEditTextEditable(edAddFranchiseOwnerPass, editable)
                isEditTextEditable(edAddFranchiseOwnerPassConfirm, editable)

                textView9.visibility = View.VISIBLE
                textView10.visibility = View.VISIBLE
                layoutAddFranchiseOwnerPass.visibility = View.VISIBLE
                layoutAddFranchiseOwnerPassConfirm.visibility = View.VISIBLE

                isEditTextEditable(edAddFranchiseReceptionistName, editable)
                isEditTextEditable(edAddFranchiseReceptionistEmail, editable)
                isEditTextEditable(edAddFranchiseReceptionistPass, editable)
                isEditTextEditable(edAddFranchiseReceptionistPassConfirm, editable)

                textView13.visibility = View.VISIBLE
                textView14.visibility = View.VISIBLE
                layoutAddFranchiseReceptionistPass.visibility = View.VISIBLE
                layoutAddFranchiseReceptionistPassConfirm.visibility = View.VISIBLE

                isEditTextEditable(edAddFranchiseInventoryName, editable)
                isEditTextEditable(edAddFranchiseInventoryEmail, editable)
                isEditTextEditable(edAddFranchiseInventoryPass, editable)
                isEditTextEditable(edAddFranchiseInventoryPassConfirm, editable)

                textView17.visibility = View.VISIBLE
                textView18.visibility = View.VISIBLE
                layoutAddFranchiseInventoryPass.visibility = View.VISIBLE
                layoutAddFranchiseInventoryPassConfirm.visibility = View.VISIBLE

                isEditTextEditable(edAddFranchiseCleaningName, editable)
                isEditTextEditable(edAddFranchiseCleaningEmail, editable)
                isEditTextEditable(edAddFranchiseCleaningPass, editable)
                isEditTextEditable(edAddFranchiseCleaningPassConfirm, editable)

                textView21.visibility = View.VISIBLE
                textView22.visibility = View.VISIBLE
                layoutAddFranchiseCleaningPass.visibility = View.VISIBLE
                layoutAddFranchiseCleaningPassConfirm.visibility = View.VISIBLE

                addFranchisePhotoRegular1.isClickable = editable
                addFranchisePhotoExclusive1.isClickable = editable
            } else {
                btnFranchiseDetailMore.visibility = View.VISIBLE
                constraintLayout5.visibility = View.GONE

                isEditTextEditable(edAddFranchiseName, editable)
                isEditTextEditable(edAddFranchiseAddress, editable)
                isEditTextEditable(edAddFranchiseContact, editable)

                isEditTextEditable(edAddFranchiseRoomRegularCount, editable)
                isEditTextEditable(edAddFranchiseRoomRegularPrice, editable)
                isEditTextEditable(edAddFranchiseRoomExclusiveCount, editable)
                isEditTextEditable(edAddFranchiseRoomExclusivePrice, editable)
                isEditTextEditable(edAddFranchiseRoomBedPrice, editable)

                isEditTextEditable(edAddFranchiseOwnerName, editable)
                isEditTextEditable(edAddFranchiseOwnerEmail, editable)
                isEditTextEditable(edAddFranchiseOwnerPass, editable)
                isEditTextEditable(edAddFranchiseOwnerPassConfirm, editable)

                textView9.visibility = View.GONE
                textView10.visibility = View.GONE
                layoutAddFranchiseOwnerPass.visibility = View.GONE
                layoutAddFranchiseOwnerPassConfirm.visibility = View.GONE

                isEditTextEditable(edAddFranchiseReceptionistName, editable)
                isEditTextEditable(edAddFranchiseReceptionistEmail, editable)
                isEditTextEditable(edAddFranchiseReceptionistPass, editable)
                isEditTextEditable(edAddFranchiseReceptionistPassConfirm, editable)

                textView13.visibility = View.GONE
                textView14.visibility = View.GONE
                layoutAddFranchiseReceptionistPass.visibility = View.GONE
                layoutAddFranchiseReceptionistPassConfirm.visibility = View.GONE

                isEditTextEditable(edAddFranchiseInventoryName, editable)
                isEditTextEditable(edAddFranchiseInventoryEmail, editable)
                isEditTextEditable(edAddFranchiseInventoryPass, editable)
                isEditTextEditable(edAddFranchiseInventoryPassConfirm, editable)

                textView17.visibility = View.GONE
                textView18.visibility = View.GONE
                layoutAddFranchiseInventoryPass.visibility = View.GONE
                layoutAddFranchiseInventoryPassConfirm.visibility = View.GONE

                isEditTextEditable(edAddFranchiseCleaningName, editable)
                isEditTextEditable(edAddFranchiseCleaningEmail, editable)
                isEditTextEditable(edAddFranchiseCleaningPass, editable)
                isEditTextEditable(edAddFranchiseCleaningPassConfirm, editable)

                textView21.visibility = View.GONE
                textView22.visibility = View.GONE
                layoutAddFranchiseCleaningPass.visibility = View.GONE
                layoutAddFranchiseCleaningPassConfirm.visibility = View.GONE

                addFranchisePhotoRegular1.isClickable = editable
                addFranchisePhotoExclusive1.isClickable = editable
            }
        }
    }

    private fun isEditTextEditable(editText: TextInputEditText, isEditable: Boolean) {
        editText.isFocusable = isEditable
        editText.isClickable = isEditable
        editText.isFocusableInTouchMode = isEditable
        editText.isCursorVisible = isEditable
    }

    private fun setupButtonMore(hotelId: String) {
        binding.btnFranchiseDetailMore.setOnClickListener {
            val popUpMenu = PopupMenu(this, binding.btnFranchiseDetailMore)
            popUpMenu.menuInflater.inflate(R.menu.menu_detail_item, popUpMenu.menu)

            popUpMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuUpdate -> {
                        val intentToAUpdateHotel =
                            Intent(this, AddFranchiseActivity::class.java)
                        intentToAUpdateHotel.putExtra(
                            FLAG_HOTEL_UI,
                            FLAG_HOTEL_UPDATE
                        )
                        intentToAUpdateHotel.putExtra(HOTEL_ID, hotelId)

                        startActivity(intentToAUpdateHotel)
                        true
                    }
                    R.id.menuDelete -> {
                        DeleteItemDialog(DeleteDialogType.MANAGE_HOTEL, hotelId).show(
                            supportFragmentManager,
                            "Delete Dialog"
                        )
                        true
                    }
                    else -> false
                }
            }

            popUpMenu.show()
        }
    }

    private fun fetchHotelInfo(hotelId: String) {
        addFranchiseViewModel.getHotel(hotelId).observe(this) { hotel ->
            when (hotel) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this@AddFranchiseActivity)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        showToast(hotel.message.toString())
                    }

                    isButtonEnabled(true)
                }
                is Resource.Loading -> {
                    showLoading(true)
                    isButtonEnabled(false)
                }
                is Resource.Message -> {
                    showLoading(false)
                    isButtonEnabled(true)

                    Timber.tag("AddFranchiseActivity").d(hotel.message)
                }
                is Resource.Success -> {
                    showLoading(false)
                    isButtonEnabled(true)

                    binding.apply {
                        hotel.data?.apply {
                            edAddFranchiseName.setText(name)
                            edAddFranchiseAddress.setText(address)
                            edAddFranchiseContact.setText(contact)

                            edAddFranchiseRoomRegularCount.setText(regularRoomCount.toString())
                            edAddFranchiseRoomRegularPrice.setText(regularRoomPrice.toString())
                            Glide.with(this@AddFranchiseActivity).load(regularRoomImage)
                                .into(addFranchisePhotoRegular1)
                            edAddFranchiseRoomExclusiveCount.setText(exclusiveRoomCount.toString())
                            edAddFranchiseRoomExclusivePrice.setText(exclusiveRoomPrice.toString())
                            Glide.with(this@AddFranchiseActivity).load(exclusiveRoomImage)
                                .into(addFranchisePhotoExclusive1)
                            edAddFranchiseRoomBedPrice.setText(extraBedPrice.toString())

                            edAddFranchiseOwnerName.setText(owner.username)
                            edAddFranchiseOwnerEmail.setText(owner.email)

                            edAddFranchiseReceptionistName.setText(receptionist.username)
                            edAddFranchiseReceptionistEmail.setText(receptionist.email)

                            edAddFranchiseInventoryName.setText(inventoryStaff.username)
                            edAddFranchiseInventoryEmail.setText(inventoryStaff.email)

                            edAddFranchiseCleaningName.setText(cleaningStaff.username)
                            edAddFranchiseCleaningEmail.setText(cleaningStaff.email)

                            savedRegularRoomImage = regularRoomImage
                            savedExclusiveRoomImage = exclusiveRoomImage
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
            when (flagImgExc) {
                1 -> {
                    exc1ImgUri = uri
                    binding.addFranchisePhotoExclusive1.setImageURI(exc1ImgUri)

                    getFileExclusive1 = uriToFile(exc1ImgUri!!, this)
                }
            }

            when (flagImgReg) {
                1 -> {
                    reg1ImgUri = uri
                    binding.addFranchisePhotoRegular1.setImageURI(reg1ImgUri)

                    getFileRegular1 = uriToFile(reg1ImgUri!!, this)
                }
            }
        } else {
            Timber.tag("Photo Picker").d("No media selected")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun handlePickImages() {
        binding.apply {
            addFranchisePhotoRegular1.setOnClickListener {
                flagImgReg = 1
                startGallery()
            }

            addFranchisePhotoExclusive1.setOnClickListener {
                flagImgExc = 1
                startGallery()
            }
        }
    }

    private fun handleEditText() {
        binding.apply {
            edAddFranchiseName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseAddress.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseRoomRegularCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseRoomRegularPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseRoomExclusiveCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseRoomExclusivePrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseRoomBedPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseOwnerName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseOwnerEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseOwnerPass.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            layoutAddFranchiseOwnerPass.setEndIconOnClickListener {
                if (edAddFranchiseOwnerPass.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    edAddFranchiseOwnerPass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    layoutAddFranchiseOwnerPass.endIconDrawable =
                        getDrawable(R.drawable.icons_no_see_pass)
                } else {
                    edAddFranchiseOwnerPass.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    layoutAddFranchiseOwnerPass.endIconDrawable =
                        getDrawable(R.drawable.icons_see_pass)
                }

                edAddFranchiseOwnerPass.setSelection(edAddFranchiseOwnerPass.text!!.length)
            }

            edAddFranchiseOwnerPassConfirm.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            layoutAddFranchiseOwnerPassConfirm.setEndIconOnClickListener {
                if (edAddFranchiseOwnerPassConfirm.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    edAddFranchiseOwnerPassConfirm.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    layoutAddFranchiseOwnerPassConfirm.endIconDrawable =
                        getDrawable(R.drawable.icons_no_see_pass)
                } else {
                    edAddFranchiseOwnerPassConfirm.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    layoutAddFranchiseOwnerPassConfirm.endIconDrawable =
                        getDrawable(R.drawable.icons_see_pass)
                }

                edAddFranchiseOwnerPassConfirm.setSelection(edAddFranchiseOwnerPassConfirm.text!!.length)
            }

            edAddFranchiseReceptionistName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseReceptionistEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseReceptionistPass.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            layoutAddFranchiseReceptionistPass.setEndIconOnClickListener {
                if (edAddFranchiseReceptionistPass.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    edAddFranchiseReceptionistPass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    layoutAddFranchiseReceptionistPass.endIconDrawable =
                        getDrawable(R.drawable.icons_no_see_pass)
                } else {
                    edAddFranchiseReceptionistPass.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    layoutAddFranchiseReceptionistPass.endIconDrawable =
                        getDrawable(R.drawable.icons_see_pass)
                }

                edAddFranchiseReceptionistPass.setSelection(edAddFranchiseReceptionistPass.text!!.length)
            }

            edAddFranchiseReceptionistPassConfirm.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            layoutAddFranchiseReceptionistPassConfirm.setEndIconOnClickListener {
                if (edAddFranchiseReceptionistPassConfirm.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    edAddFranchiseReceptionistPassConfirm.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    layoutAddFranchiseReceptionistPassConfirm.endIconDrawable =
                        getDrawable(R.drawable.icons_no_see_pass)
                } else {
                    edAddFranchiseReceptionistPassConfirm.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    layoutAddFranchiseReceptionistPassConfirm.endIconDrawable =
                        getDrawable(R.drawable.icons_see_pass)
                }

                edAddFranchiseReceptionistPassConfirm.setSelection(
                    edAddFranchiseReceptionistPassConfirm.text!!.length
                )
            }

            edAddFranchiseInventoryName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseInventoryEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseInventoryPass.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            layoutAddFranchiseInventoryPass.setEndIconOnClickListener {
                if (edAddFranchiseInventoryPass.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    edAddFranchiseInventoryPass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    layoutAddFranchiseInventoryPass.endIconDrawable =
                        getDrawable(R.drawable.icons_no_see_pass)
                } else {
                    edAddFranchiseInventoryPass.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    layoutAddFranchiseInventoryPass.endIconDrawable =
                        getDrawable(R.drawable.icons_see_pass)
                }

                edAddFranchiseInventoryPass.setSelection(edAddFranchiseInventoryPass.text!!.length)
            }

            edAddFranchiseInventoryPassConfirm.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            layoutAddFranchiseInventoryPassConfirm.setEndIconOnClickListener {
                if (edAddFranchiseInventoryPassConfirm.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    edAddFranchiseInventoryPassConfirm.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    layoutAddFranchiseInventoryPassConfirm.endIconDrawable =
                        getDrawable(R.drawable.icons_no_see_pass)
                } else {
                    edAddFranchiseInventoryPassConfirm.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    layoutAddFranchiseInventoryPassConfirm.endIconDrawable =
                        getDrawable(R.drawable.icons_see_pass)
                }

                edAddFranchiseInventoryPassConfirm.setSelection(edAddFranchiseInventoryPassConfirm.text!!.length)
            }

            edAddFranchiseCleaningName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseCleaningEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            edAddFranchiseCleaningPass.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            layoutAddFranchiseCleaningPass.setEndIconOnClickListener {
                if (edAddFranchiseCleaningPass.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    edAddFranchiseCleaningPass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    layoutAddFranchiseCleaningPass.endIconDrawable =
                        getDrawable(R.drawable.icons_no_see_pass)
                } else {
                    edAddFranchiseCleaningPass.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    layoutAddFranchiseCleaningPass.endIconDrawable =
                        getDrawable(R.drawable.icons_see_pass)
                }

                edAddFranchiseCleaningPass.setSelection(edAddFranchiseCleaningPass.text!!.length)
            }

            edAddFranchiseCleaningPassConfirm.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            })

            layoutAddFranchiseCleaningPassConfirm.setEndIconOnClickListener {
                if (edAddFranchiseCleaningPassConfirm.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    edAddFranchiseCleaningPassConfirm.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    layoutAddFranchiseCleaningPassConfirm.endIconDrawable =
                        getDrawable(R.drawable.icons_no_see_pass)
                } else {
                    edAddFranchiseCleaningPassConfirm.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    layoutAddFranchiseCleaningPassConfirm.endIconDrawable =
                        getDrawable(R.drawable.icons_see_pass)
                }

                edAddFranchiseCleaningPassConfirm.setSelection(edAddFranchiseCleaningPassConfirm.text!!.length)
            }
        }
    }

    private fun handleAddHotelSaveButton() {
        binding.apply {
            btnSave.setOnClickListener {
                isButtonEnabled(false)
                showLoading(true)

                val hotelName = edAddFranchiseName.text.toString()
                val hotelAddress = edAddFranchiseAddress.text.toString()
                val hotelContact = edAddFranchiseContact.text.toString()

                val regularCount =
                    if (edAddFranchiseRoomRegularCount.text.toString() == "") 0
                    else edAddFranchiseRoomRegularCount.text.toString().toInt()
                val regularPrice =
                    if (edAddFranchiseRoomRegularPrice.text.toString() == "") 0
                    else edAddFranchiseRoomRegularPrice.text.toString().toInt()
                val exclusiveCount =
                    if (edAddFranchiseRoomExclusiveCount.text.toString() == "") 0
                    else edAddFranchiseRoomExclusiveCount.text.toString().toInt()
                val exclusivePrice =
                    if (edAddFranchiseRoomExclusivePrice.text.toString() == "") 0
                    else edAddFranchiseRoomExclusivePrice.text.toString().toInt()
                val bedPrice =
                    if (edAddFranchiseRoomBedPrice.text.toString() == "") 0
                    else edAddFranchiseRoomBedPrice.text.toString().toInt()

                val ownerName = edAddFranchiseOwnerName.text.toString()
                val ownerEmail = edAddFranchiseOwnerEmail.text.toString()
                val ownerPass = edAddFranchiseOwnerPass.text.toString()

                val receptionistName = edAddFranchiseReceptionistName.text.toString()
                val receptionistEmail = edAddFranchiseReceptionistEmail.text.toString()
                val receptionistPass = edAddFranchiseReceptionistPass.text.toString()

                val inventoryName = edAddFranchiseInventoryName.text.toString()
                val inventoryEmail = edAddFranchiseInventoryEmail.text.toString()
                val inventoryPass = edAddFranchiseInventoryPass.text.toString()

                val cleaningName = edAddFranchiseCleaningName.text.toString()
                val cleaningEmail = edAddFranchiseCleaningEmail.text.toString()
                val cleaningPass = edAddFranchiseCleaningPass.text.toString()

                val fileExclusive1 = reduceFileImage(getFileExclusive1 as File)
                val requestImageFileExclusive1 =
                    fileExclusive1.asRequestBody("image/jpeg".toMediaType())
                val imageMultipartExclusive1: MultipartBody.Part =
                    MultipartBody.Part.createFormData(
                        EXCLUSIVE_PHOTO,
                        "Exclusive_Room_Photo_${DateUtils.getCompleteCurentDateTime()}",
                        requestImageFileExclusive1
                    )

                val fileRegular1 = reduceFileImage(getFileRegular1 as File)
                val requestImageFileRegular1 =
                    fileRegular1.asRequestBody("image/jpeg".toMediaType())
                val imageMultipartRegular1: MultipartBody.Part =
                    MultipartBody.Part.createFormData(
                        REGULAR_PHOTO,
                        "Regular_Room_Photo_${DateUtils.getCompleteCurentDateTime()}",
                        requestImageFileRegular1
                    )

                val listOfRoomImage =
                    listOf(
                        imageMultipartRegular1,
                        imageMultipartExclusive1
                    )

                addFranchiseViewModel.executeAddHotel(
                    listOfRoomImage,
                    hotelName,
                    hotelAddress,
                    hotelContact,
                    regularCount,
                    exclusiveCount,
                    regularPrice,
                    exclusivePrice,
                    bedPrice,
                    ownerName,
                    ownerEmail,
                    ownerPass,
                    receptionistName,
                    receptionistEmail,
                    receptionistPass,
                    cleaningName,
                    cleaningEmail,
                    cleaningPass,
                    inventoryName,
                    inventoryEmail,
                    inventoryPass
                ).observe(this@AddFranchiseActivity) { hotel ->
                    when (hotel) {
                        is Resource.Error -> {
                            showLoading(false)

                            if (!isInternetAvailable(this@AddFranchiseActivity)) {
                                showToast(getString(R.string.check_internet))
                            } else {
                                showToast(hotel.message.toString())
                            }

                            isButtonEnabled(true)
                        }
                        is Resource.Loading -> {
                            showLoading(true)
                            isButtonEnabled(false)
                        }
                        is Resource.Message -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            Timber.tag("AddFranchiseActivity").d(hotel.message)
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            showToast("Cabang hotel baru telah berhasil ditambahkan")

                            val intentToManageFranchise = Intent(
                                this@AddFranchiseActivity,
                                ManageFranchiseActivity::class.java
                            )
                            startActivity(intentToManageFranchise)
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun checkForms() {
        binding.apply {
            val hotelName = edAddFranchiseName.text.toString()
            val hotelAddress = edAddFranchiseAddress.text.toString()
            val hotelContact = edAddFranchiseContact.text.toString()

            val regularCount =
                if (edAddFranchiseRoomRegularCount.text.toString() == "") 0
                else edAddFranchiseRoomRegularCount.text.toString().toInt()
            val regularPrice =
                if (edAddFranchiseRoomRegularPrice.text.toString() == "") 0
                else edAddFranchiseRoomRegularPrice.text.toString().toInt()
            val exclusiveCount =
                if (edAddFranchiseRoomExclusiveCount.text.toString() == "") 0
                else edAddFranchiseRoomExclusiveCount.text.toString().toInt()
            val exclusivePrice =
                if (edAddFranchiseRoomExclusivePrice.text.toString() == "") 0
                else edAddFranchiseRoomExclusivePrice.text.toString().toInt()
            val bedPrice =
                if (edAddFranchiseRoomBedPrice.text.toString() == "") 0
                else edAddFranchiseRoomBedPrice.text.toString().toInt()

            val ownerName = edAddFranchiseOwnerName.text.toString()
            val ownerEmail = edAddFranchiseOwnerEmail.text.toString()
            val ownerPass = edAddFranchiseOwnerPass.text.toString()
            val ownerPassConfirm = edAddFranchiseOwnerPassConfirm.text.toString()

            val receptionistName = edAddFranchiseReceptionistName.text.toString()
            val receptionistEmail = edAddFranchiseReceptionistEmail.text.toString()
            val receptionistPass = edAddFranchiseReceptionistPass.text.toString()
            val receptionistPassConfirm = edAddFranchiseReceptionistPassConfirm.text.toString()

            val inventoryName = edAddFranchiseInventoryName.text.toString()
            val inventoryEmail = edAddFranchiseInventoryEmail.text.toString()
            val inventoryPass = edAddFranchiseInventoryPass.text.toString()
            val inventoryPassConfirm = edAddFranchiseInventoryPassConfirm.text.toString()

            val cleaningName = edAddFranchiseCleaningName.text.toString()
            val cleaningEmail = edAddFranchiseCleaningEmail.text.toString()
            val cleaningPass = edAddFranchiseCleaningPass.text.toString()
            val cleaningPassConfirm = edAddFranchiseCleaningPassConfirm.text.toString()


            // OWNER
            if (!Patterns.EMAIL_ADDRESS.matcher(ownerEmail).matches() && ownerEmail != "") {
                layoutAddFranchiseOwnerEmail.error = getString(R.string.wrong_email_format)
            } else {
                layoutAddFranchiseOwnerEmail.error = null
            }

            if (ownerPass.length < 8) {
                layoutAddFranchiseOwnerPass.error =
                    getString(R.string.wrong_password_format)
            } else {
                layoutAddFranchiseOwnerPass.error = null
            }

            if (ownerPassConfirm.length < 8) {
                layoutAddFranchiseOwnerPassConfirm.error =
                    getString(R.string.wrong_password_format)
            } else {
                if (ownerPass != ownerPassConfirm) {
                    layoutAddFranchiseOwnerPass.error = getString(R.string.diff_password)
                    layoutAddFranchiseOwnerPassConfirm.error =
                        getString(R.string.diff_password)
                } else {
                    layoutAddFranchiseOwnerPass.error = null
                    layoutAddFranchiseOwnerPassConfirm.error = null
                }
            }
            // OWNER

            // RECEPTIONIST
            if (!Patterns.EMAIL_ADDRESS.matcher(receptionistEmail)
                    .matches() && receptionistEmail != ""
            ) {
                layoutAddFranchiseReceptionistEmail.error =
                    getString(R.string.wrong_email_format)
            } else {
                layoutAddFranchiseReceptionistEmail.error = null
            }

            if (receptionistPass.length < 8) {
                layoutAddFranchiseReceptionistPass.error =
                    getString(R.string.wrong_password_format)
            } else {
                layoutAddFranchiseReceptionistPass.error = null
            }

            if (receptionistPassConfirm.length < 8) {
                layoutAddFranchiseReceptionistPassConfirm.error =
                    getString(R.string.wrong_password_format)
            } else {
                if (receptionistPass != receptionistPassConfirm) {
                    layoutAddFranchiseReceptionistPass.error =
                        getString(R.string.diff_password)
                    layoutAddFranchiseReceptionistPassConfirm.error =
                        getString(R.string.diff_password)
                } else {
                    layoutAddFranchiseReceptionistPass.error = null
                    layoutAddFranchiseReceptionistPassConfirm.error = null
                }
            }
            // RECEPTIONIST

            // INVENTORY
            if (!Patterns.EMAIL_ADDRESS.matcher(inventoryEmail)
                    .matches() && inventoryEmail != ""
            ) {
                layoutAddFranchiseInventoryEmail.error =
                    getString(R.string.wrong_email_format)
            } else {
                layoutAddFranchiseInventoryEmail.error = null
            }

            if (inventoryPass.length < 8) {
                layoutAddFranchiseInventoryPass.error =
                    getString(R.string.wrong_password_format)
            } else {
                layoutAddFranchiseInventoryPass.error = null
            }

            if (inventoryPassConfirm.length < 8) {
                layoutAddFranchiseInventoryPassConfirm.error =
                    getString(R.string.wrong_password_format)
            } else {
                if (inventoryPass != inventoryPassConfirm) {
                    layoutAddFranchiseInventoryPass.error =
                        getString(R.string.diff_password)
                    layoutAddFranchiseInventoryPassConfirm.error =
                        getString(R.string.diff_password)
                } else {
                    layoutAddFranchiseInventoryPass.error = null
                    layoutAddFranchiseInventoryPassConfirm.error = null
                }
            }
            // INVENTORY

            // CLEANING
            if (!Patterns.EMAIL_ADDRESS.matcher(cleaningEmail)
                    .matches() && cleaningEmail != ""
            ) {
                layoutAddFranchiseCleaningEmail.error =
                    getString(R.string.wrong_email_format)
            } else {
                layoutAddFranchiseCleaningEmail.error = null
            }

            if (cleaningPass.length < 8) {
                layoutAddFranchiseCleaningPass.error =
                    getString(R.string.wrong_password_format)
            } else {
                layoutAddFranchiseCleaningPass.error = null
            }

            if (cleaningPassConfirm.length < 8) {
                layoutAddFranchiseCleaningPassConfirm.error =
                    getString(R.string.wrong_password_format)
            } else {
                if (cleaningPass != cleaningPassConfirm) {
                    layoutAddFranchiseCleaningPass.error =
                        getString(R.string.diff_password)
                    layoutAddFranchiseCleaningPassConfirm.error =
                        getString(R.string.diff_password)
                } else {
                    layoutAddFranchiseCleaningPass.error = null
                    layoutAddFranchiseCleaningPassConfirm.error = null
                }
            }
            // CLEANING

            isButtonEnabled(
                hotelName.isNotEmpty() && layoutAddFranchiseName.error == null &&
                        hotelAddress.isNotEmpty() && layoutAddFranchiseAddress.error == null &&
                        hotelContact.isNotEmpty() && layoutAddFranchiseContact.error == null &&
                        regularCount >= MIN_ROOM_COUNT && regularCount <= MAX_ROOM_COUNT &&
                        regularPrice >= MIN_ROOM_PRICE && regularPrice <= MAX_ROOM_PRICE &&
                        exclusiveCount > MIN_ROOM_COUNT && exclusiveCount <= MAX_ROOM_COUNT &&
                        exclusivePrice >= MIN_ROOM_PRICE && exclusivePrice <= MAX_ROOM_PRICE &&
                        bedPrice >= MIN_BED_PRICE && bedPrice <= MAX_BED_PRICE &&
                        ownerName.isNotEmpty() && layoutAddFranchiseOwnerName.error == null &&
                        ownerEmail.isNotEmpty() && layoutAddFranchiseOwnerEmail.error == null &&
                        ownerPass.isNotEmpty() && layoutAddFranchiseOwnerPass.error == null &&
                        receptionistName.isNotEmpty() && layoutAddFranchiseReceptionistName.error == null &&
                        receptionistEmail.isNotEmpty() && layoutAddFranchiseReceptionistEmail.error == null &&
                        receptionistPass.isNotEmpty() && layoutAddFranchiseReceptionistPass.error == null &&
                        inventoryName.isNotEmpty() && layoutAddFranchiseInventoryName.error == null &&
                        inventoryEmail.isNotEmpty() && layoutAddFranchiseInventoryEmail.error == null &&
                        inventoryPass.isNotEmpty() && layoutAddFranchiseInventoryPass.error == null &&
                        cleaningName.isNotEmpty() && layoutAddFranchiseCleaningName.error == null &&
                        cleaningEmail.isNotEmpty() && layoutAddFranchiseCleaningEmail.error == null &&
                        cleaningPass.isNotEmpty() && layoutAddFranchiseCleaningPass.error == null &&
                        getFileRegular1 != null &&
                        getFileExclusive1 != null
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

    private fun showLoading(isLoading: Boolean) {
        binding.refAddFranchise.isRefreshing = isLoading
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val REGULAR_PHOTO = "image"
        private const val EXCLUSIVE_PHOTO = "image"

        private const val EXCLUSIVE_FILENAME = "exclusive_room_photo"
        private const val REGULAR_FILENAME = "regular_room_photo"

        private const val MIN_ROOM_COUNT = 1
        private const val MAX_ROOM_COUNT = 30

        private const val MIN_ROOM_PRICE = 10000
        private const val MAX_ROOM_PRICE = 1000000

        private const val MIN_BED_PRICE = 10000
        private const val MAX_BED_PRICE = 500000

        private const val FLAG_HOTEL_UI = "flag_hotel_UI"
        private const val FLAG_HOTEL_DETAIL = "hotel_detail"
        private const val FLAG_HOTEL_UPDATE = "hotel_update"

        private const val HOTEL_ID = "hotel_id"
    }
}