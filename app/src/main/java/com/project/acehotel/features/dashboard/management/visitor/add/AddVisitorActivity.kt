package com.project.acehotel.features.dashboard.management.visitor.add

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.*
import com.project.acehotel.databinding.ActivityAddVisitorBinding
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class AddVisitorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddVisitorBinding

    private val addVisitorViewModel: AddVisitorViewModel by viewModels()

    private var imgUri: Uri? = null
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isButtonEnabled(false)

        handleButtonBack()

        setupActionBar()

        handleEditText()

        handlePickImages()

        setupUI()

        enableRefresh(false)

        validateToken()
    }

    private fun validateToken() {
        addVisitorViewModel.getRefreshToken().observe(this) { token ->
            if (token.isEmpty() || token == "") {
                TokenExpiredDialog().show(supportFragmentManager, "Token Expired Dialog")
            }
        }
    }

    private fun setupUI() {
        val isUpdate = intent.getBooleanExtra(VISITOR_UPDATE, false)

        if (isUpdate) {
            fetchVisitorInfo(intent.getStringExtra(VISITOR_ID) ?: "")
        } else {
            handleSaveButton()
        }
    }

    private fun fetchVisitorInfo(id: String) {
        addVisitorViewModel.getVisitorDetail(id).observe(this) { visitor ->
            when (visitor) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this@AddVisitorActivity)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        showToast(visitor.message.toString())
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

                    Timber.tag("AddVisitorActivity").d(visitor.message)
                }
                is Resource.Success -> {
                    showLoading(false)
                    isButtonEnabled(true)

                    binding.apply {
                        edAddVisitorName.setText(visitor.data?.name)
                        edAddVisitorNik.setText(visitor.data?.identity_num)
                        edAddVisitorPhone.setText(visitor.data?.phone)
                        edAddVisitorEmail.setText(visitor.data?.email)
                        edAddVisitorAddress.setText(visitor.data?.address)

                        Glide.with(this@AddVisitorActivity).load(visitor.data?.identityImage)
                            .into(ivVisitorKtp)
                    }
                }
            }
        }
    }

    private fun handleEditText() {
        binding.apply {
            edAddVisitorAddress.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }

            })

            edAddVisitorName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }

            })

            edAddVisitorEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }

            })

            edAddVisitorPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }

            })

            edAddVisitorNik.addTextChangedListener(object : TextWatcher {
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

    private fun checkForms() {
        binding.apply {
            val name = edAddVisitorName.text.toString()
            val nik = edAddVisitorNik.text.toString()
            val phone = edAddVisitorPhone.text.toString()
            val address = edAddVisitorAddress.text.toString()
            val email = edAddVisitorEmail.text.toString()

            isButtonEnabled(
                name.isNotEmpty() &&
                        nik.isNotEmpty() &&
                        phone.isNotEmpty() &&
                        address.isNotEmpty() &&
                        email.isNotEmpty() &&
                        getFile != null
            )
        }
    }

    private fun handleSaveButton() {
        binding.apply {
            btnSave.setOnClickListener {
                isButtonEnabled(false)
                enableRefresh(true)
                showLoading(true)

                val name = edAddVisitorName.text.toString()
                val nik = edAddVisitorNik.text.toString()
                val phone = edAddVisitorPhone.text.toString()
                val address = edAddVisitorAddress.text.toString()
                val email = edAddVisitorEmail.text.toString()

                val file = reduceFileImage(getFile as File)
                val requestImageFile =
                    file.asRequestBody("image/jpeg".toMediaType())
                val imageMultipartRegular1: MultipartBody.Part =
                    MultipartBody.Part.createFormData(
                        VISITOR_PHOTO,
                        "Visitor_Photo_${DateUtils.getCompleteCurrentDateTime()}",
                        requestImageFile
                    )

                val visitorImage =
                    listOf(
                        imageMultipartRegular1,
                    )

                addVisitorViewModel.executeAddVisitor(
                    visitorImage,
                    name,
                    address,
                    phone,
                    email,
                    nik
                ).observe(this@AddVisitorActivity) { visitor ->
                    when (visitor) {
                        is Resource.Error -> {
                            showLoading(false)

                            if (!isInternetAvailable(this@AddVisitorActivity)) {
                                showToast(getString(R.string.check_internet))
                            } else {
                                showToast(visitor.message.toString())
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

                            Timber.tag("AddVisitorActivity").d(visitor.message)
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            showToast("Pengunjung baru berhasil ditambahkan")

                            finish()
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
            binding.ivVisitorKtp.setImageURI(imgUri)

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
        binding.ivVisitorKtp.setOnClickListener {
            startGallery()
        }
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refAddVisitor.isRefreshing = isLoading
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnSave.isEnabled = isEnabled
    }

    private fun enableRefresh(isDisable: Boolean) {
        binding.refAddVisitor.isEnabled = isDisable
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val VISITOR_PHOTO = "image"
        private const val VISITOR_ID = "visitor_id"
        private const val VISITOR_UPDATE = "visitor_update"
    }
}