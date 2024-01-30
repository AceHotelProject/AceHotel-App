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
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.R
import com.project.acehotel.databinding.ActivityAddFranchiseBinding
import timber.log.Timber

class AddFranchiseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFranchiseBinding

    private var flagImgReg = 0
    private var flagImgExc = 0

    private var reg1ImgUri: Uri? = null
    private var reg2ImgUri: Uri? = null
    private var reg3ImgUri: Uri? = null

    private var exc1ImgUri: Uri? = null
    private var exc2ImgUri: Uri? = null
    private var exc3ImgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFranchiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isButtonEnabled(false)

        setupActionBar()

        handleButtonBack()

        handleEditText()

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
                    checkForms()
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
                    checkForms()
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
                    checkForms()
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
                    checkForms()
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
                    checkForms()
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
                    checkForms()
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
                    checkForms()
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
                    checkForms()
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
                    checkForms()
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

//            edAddFranchiseInventoryName.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                    checkForms()
//                }
//            })
//
//            edAddFranchiseInventoryEmail.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                    checkForms()
//                }
//            })
//
//            edAddFranchiseInventoryPass.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                    checkForms()
//                }
//            })
//
//            edAddFranchiseInventoryPassConfirm.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                    checkForms()
//                }
//            })

//            edAddFranchiseCleaningName.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                    checkForms()
//                }
//            })
//
//            edAddFranchiseCleaningEmail.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                    checkForms()
//                }
//            })
//
//            edAddFranchiseCleaningPass.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                    checkForms()
//                }
//            })
//
//            edAddFranchiseCleaningPassConfirm.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    checkForms()
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                    checkForms()
//                }
//            })
        }
    }

    private fun handleSaveButton() {
        checkForms()

        binding.btnSave.setOnClickListener {

        }
    }

    private fun checkForms() {
        binding.apply {
            val hotelName = edAddFranchiseName.text.toString()
            val hotelAddress = edAddFranchiseAddress.text.toString()
            val regularCount =
                if (edAddFranchiseRoomRegularCount.text.toString() == "") 0 else edAddFranchiseRoomRegularCount.text.toString()
                    .toInt()
            val regularPrice =
                if (edAddFranchiseRoomRegularPrice.text.toString() == "") 0 else edAddFranchiseRoomRegularPrice.text.toString()
                    .toInt()

            val exclusiveCount =
                if (edAddFranchiseRoomExclusiveCount.text.toString() == "") 0 else edAddFranchiseRoomExclusiveCount.text.toString()
                    .toInt()
            val exclusivePrice =
                if (edAddFranchiseRoomExclusivePrice.text.toString() == "") 0 else edAddFranchiseRoomExclusivePrice.text.toString()
                    .toInt()
            val bedPrice =
                if (edAddFranchiseRoomBedPrice.text.toString() == "") 0 else edAddFranchiseRoomBedPrice.text.toString()
                    .toInt()

            val ownerName = edAddFranchiseOwnerName.text.toString()
            val ownerEmail = edAddFranchiseOwnerEmail.text.toString()
            val ownerPass = edAddFranchiseOwnerPass.text.toString()
            val ownerPassConfirm = edAddFranchiseOwnerPassConfirm.text.toString()

            val receptionistName = edAddFranchiseReceptionistName.text.toString()
            val receptionistEmail = edAddFranchiseReceptionistEmail.text.toString()
            val receptionistPass = edAddFranchiseReceptionistPass.text.toString()
            val receptionistPassConfirm = edAddFranchiseReceptionistPassConfirm.text.toString()

//            val inventoryName = edAddFranchiseInventoryName.text.toString()
//            val inventoryEmail = edAddFranchiseInventoryEmail.text.toString()
//            val inventoryPass = edAddFranchiseInventoryPass.text.toString()
//            val inventoryPassConfirm = edAddFranchiseInventoryPassConfirm.text.toString()
//
//            val cleaningName = edAddFranchiseCleaningName.text.toString()
//            val cleaningEmail = edAddFranchiseCleaningEmail.text.toString()
//            val cleaningPass = edAddFranchiseCleaningPass.text.toString()
//            val cleaningPassConfirm = edAddFranchiseCleaningPassConfirm.text.toString()


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

            isButtonEnabled(
                hotelName.isNotEmpty() && hotelAddress.isNotEmpty() && regularCount > 0 && regularPrice > 1000 && exclusiveCount > 0 && exclusivePrice > 1000 && bedPrice > 1000 &&
                        ownerName.isNotEmpty() && ownerEmail.isNotEmpty() && ownerPass.isNotEmpty() && receptionistName.isNotEmpty() && receptionistEmail.isNotEmpty() && receptionistPass.isNotEmpty()
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

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}