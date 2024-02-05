package com.project.acehotel.features.dashboard.profile.add_franchise

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.reduceFileImage
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.core.utils.uriToFile
import com.project.acehotel.databinding.ActivityAddFranchiseBinding
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
    private var reg2ImgUri: Uri? = null
    private var reg3ImgUri: Uri? = null

    private var exc1ImgUri: Uri? = null
    private var exc2ImgUri: Uri? = null
    private var exc3ImgUri: Uri? = null

    private var getFileRegular: File? = null
    private var getFileExclusive: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isButtonEnabled(true)

        setupActionBar()

        handleButtonBack()

//        handleEditText()

        handlePickImages()

        handleSaveButton()
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            when (flagImgExc) {
                1 -> {
                    exc1ImgUri = uri
                    binding.addFranchisePhotoExclusive1.setImageURI(exc1ImgUri)

                    getFileExclusive = uriToFile(exc1ImgUri!!, this)
                }
                2 -> {
                    exc2ImgUri = uri
                    binding.addFranchisePhotoExclusive2.setImageURI(exc2ImgUri)
                }
                3 -> {
                    exc3ImgUri = uri
                    binding.addFranchisePhotoExclusive3.setImageURI(exc3ImgUri)
                }
            }

            when (flagImgReg) {
                1 -> {
                    reg1ImgUri = uri
                    binding.addFranchisePhotoRegular1.setImageURI(reg1ImgUri)

                    getFileRegular = uriToFile(reg1ImgUri!!, this)
                }
                2 -> {
                    reg2ImgUri = uri
                    binding.addFranchisePhotoRegular2.setImageURI(reg2ImgUri)
                }
                3 -> {
                    reg3ImgUri = uri
                    binding.addFranchisePhotoRegular3.setImageURI(reg3ImgUri)
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

            addFranchisePhotoRegular2.setOnClickListener {
                flagImgReg = 2
                startGallery()
            }

            addFranchisePhotoRegular3.setOnClickListener {
                flagImgReg = 3
                startGallery()
            }

            addFranchisePhotoExclusive1.setOnClickListener {
                flagImgExc = 1
                startGallery()
            }

            addFranchisePhotoExclusive2.setOnClickListener {
                flagImgExc = 2
                startGallery()
            }

            addFranchisePhotoExclusive3.setOnClickListener {
                flagImgExc = 3
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
        }
    }

    private fun handleSaveButton() {
        binding.apply {
            btnSave.setOnClickListener {
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

//                val fileExclusive = reduceFileImage(getFileExclusive as File)
//                val requestImageFileExclusive =
//                    fileExclusive.asRequestBody("image/jpeg".toMediaType())
//                val imageMultipartExclusive: MultipartBody.Part = MultipartBody.Part.createFormData(
//                    EXCLUSIVE_PHOTO,
//                    "Exclusive Room Photo",
//                    requestImageFileExclusive
//                )

                val fileRegular = reduceFileImage(getFileRegular as File)
                val requestImageFileRegular = fileRegular.asRequestBody("image/jpeg".toMediaType())
                val imageMultipartRegular: MultipartBody.Part = MultipartBody.Part.createFormData(
                    REGULAR_PHOTO,
                    "Regular Room Photo",
                    requestImageFileRegular
                )

                addFranchiseViewModel.uploadImage(imageMultipartRegular)
                    .observe(this@AddFranchiseActivity) { image ->
                        when (image) {
                            is Resource.Error -> {
                                showLoading(false)
                            }
                            is Resource.Loading -> {
                                showLoading(true)
                            }
                            is Resource.Message -> {
                                showLoading(false)
                            }
                            is Resource.Success -> {
                                showLoading(false)

                                showToast(image.data?.get(1) ?: "")
                            }
                        }
                    }
            }

//            btnSave.setOnClickListener {
//                addFranchiseViewModel.addHotel(
//                    hotelName,
//                    hotelAddress,
//                    hotelContact,
//                    regularCount,
//                    imageMultipartRegular,
//                    exclusiveCount,
//                    imageMultipartExclusive,
//                    regularPrice,
//                    exclusivePrice,
//                    bedPrice,
//                    ownerName,
//                    ownerEmail,
//                    ownerPass,
//                    receptionistName,
//                    receptionistEmail,
//                    receptionistPass,
//                    cleaningName,
//                    cleaningEmail,
//                    cleaningPass,
//                    inventoryName,
//                    inventoryEmail,
//                    inventoryPass
//                ).observe(this@AddFranchiseActivity) { hotel ->
//                    when (hotel) {
//                        is Resource.Error -> {
//                            showLoading(false)
//
//                            if (!isInternetAvailable(this@AddFranchiseActivity)) {
//                                showToast(getString(R.string.check_internet))
//                            } else {
//                                showToast(hotel.message.toString())
//                            }
//                        }
//                        is Resource.Loading -> {
//                            showLoading(true)
//                        }
//                        is Resource.Message -> {
//                            showLoading(false)
//                            Timber.tag("AddFranchiseActivity").d(hotel.message)
//                        }
//                        is Resource.Success -> {
//                            showLoading(false)
//
//                            showToast("Cabang hotel baru telah berhasil ditambahkan")
//
//                            val intentToManageFranchise = Intent(
//                                this@AddFranchiseActivity,
//                                ManageFranchiseActivity::class.java
//                            )
//                            startActivity(intentToManageFranchise)
//                            finish()
//                        }
//                    }
//                }
//            }
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
                binding.layoutAddFranchiseOwnerEmail.error = getString(R.string.wrong_email_format)
            } else {
                binding.layoutAddFranchiseOwnerEmail.error = null
            }

            if (ownerPass.length < 8) {
                binding.layoutAddFranchiseOwnerPass.error =
                    getString(R.string.wrong_password_format)
            } else {
                binding.layoutAddFranchiseOwnerPass.error = null
            }

            if (ownerPassConfirm.length < 8) {
                binding.layoutAddFranchiseOwnerPassConfirm.error =
                    getString(R.string.wrong_password_format)
            } else {
                if (ownerPass != ownerPassConfirm) {
                    binding.layoutAddFranchiseOwnerPass.error = getString(R.string.diff_password)
                    binding.layoutAddFranchiseOwnerPassConfirm.error =
                        getString(R.string.diff_password)
                } else {
                    binding.layoutAddFranchiseOwnerPass.error = null
                    binding.layoutAddFranchiseOwnerPassConfirm.error = null
                }
            }
            // OWNER

            // RECEPTIONIST
            if (!Patterns.EMAIL_ADDRESS.matcher(receptionistEmail)
                    .matches() && receptionistEmail != ""
            ) {
                binding.layoutAddFranchiseReceptionistEmail.error =
                    getString(R.string.wrong_email_format)
            } else {
                binding.layoutAddFranchiseReceptionistEmail.error = null
            }

            if (receptionistPass.length < 8) {
                binding.layoutAddFranchiseReceptionistPass.error =
                    getString(R.string.wrong_password_format)
            } else {
                binding.layoutAddFranchiseReceptionistPass.error = null
            }

            if (receptionistPassConfirm.length < 8) {
                binding.layoutAddFranchiseReceptionistPassConfirm.error =
                    getString(R.string.wrong_password_format)
            } else {
                if (receptionistPass != receptionistPassConfirm) {
                    binding.layoutAddFranchiseReceptionistPass.error =
                        getString(R.string.diff_password)
                    binding.layoutAddFranchiseReceptionistPassConfirm.error =
                        getString(R.string.diff_password)
                } else {
                    binding.layoutAddFranchiseReceptionistPass.error = null
                    binding.layoutAddFranchiseReceptionistPassConfirm.error = null
                }
            }
            // RECEPTIONIST

            // INVENTORY
            if (!Patterns.EMAIL_ADDRESS.matcher(inventoryEmail)
                    .matches() && inventoryEmail != ""
            ) {
                binding.layoutAddFranchiseInventoryEmail.error =
                    getString(R.string.wrong_email_format)
            } else {
                binding.layoutAddFranchiseInventoryEmail.error = null
            }

            if (inventoryPass.length < 8) {
                binding.layoutAddFranchiseInventoryPass.error =
                    getString(R.string.wrong_password_format)
            } else {
                binding.layoutAddFranchiseInventoryPass.error = null
            }

            if (inventoryPassConfirm.length < 8) {
                binding.layoutAddFranchiseInventoryPass.error =
                    getString(R.string.wrong_password_format)
            } else {
                if (inventoryPass != inventoryPassConfirm) {
                    binding.layoutAddFranchiseInventoryPass.error =
                        getString(R.string.diff_password)
                    binding.layoutAddFranchiseInventoryPassConfirm.error =
                        getString(R.string.diff_password)
                } else {
                    binding.layoutAddFranchiseInventoryPass.error = null
                    binding.layoutAddFranchiseInventoryPassConfirm.error = null
                }
            }
            // INVENTORY

            // CLEANING
            if (!Patterns.EMAIL_ADDRESS.matcher(cleaningEmail)
                    .matches() && cleaningEmail != ""
            ) {
                binding.layoutAddFranchiseCleaningEmail.error =
                    getString(R.string.wrong_email_format)
            } else {
                binding.layoutAddFranchiseCleaningEmail.error = null
            }

            if (cleaningPass.length < 8) {
                binding.layoutAddFranchiseCleaningPass.error =
                    getString(R.string.wrong_password_format)
            } else {
                binding.layoutAddFranchiseCleaningPass.error = null
            }

            if (cleaningPassConfirm.length < 8) {
                binding.layoutAddFranchiseCleaningPassConfirm.error =
                    getString(R.string.wrong_password_format)
            } else {
                if (cleaningPass != cleaningPassConfirm) {
                    binding.layoutAddFranchiseCleaningPass.error =
                        getString(R.string.diff_password)
                    binding.layoutAddFranchiseCleaningPassConfirm.error =
                        getString(R.string.diff_password)
                } else {
                    binding.layoutAddFranchiseCleaningPass.error = null
                    binding.layoutAddFranchiseCleaningPassConfirm.error = null
                }
            }
            // CLEANING

            isButtonEnabled(
                hotelName.isNotEmpty() && hotelAddress.isNotEmpty() && hotelContact.isNotEmpty() && regularCount > 0 && regularPrice > 1000 && exclusiveCount > 0 && exclusivePrice > 1000 && bedPrice > 1000 &&
                        ownerName.isNotEmpty() && ownerEmail.isNotEmpty() && ownerPass.isNotEmpty() && receptionistName.isNotEmpty() && receptionistEmail.isNotEmpty() && receptionistPass.isNotEmpty() &&
                        inventoryName.isNotEmpty() && inventoryEmail.isNotEmpty() && inventoryPass.isNotEmpty() && cleaningName.isNotEmpty() && cleaningEmail.isNotEmpty() && cleaningPass.isNotEmpty() &&
                        getFileRegular != null && getFileExclusive != null
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
    }
}