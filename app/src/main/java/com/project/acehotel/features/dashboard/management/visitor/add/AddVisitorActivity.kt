package com.project.acehotel.features.dashboard.management.visitor.add

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.reduceFileImage
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.core.utils.uriToFile
import com.project.acehotel.databinding.ActivityAddVisitorBinding
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

        handleSaveButton()

        handleEditText()

        handleEditText()
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
                        "Regular_Room_Photo",
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

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val VISITOR_PHOTO = "path_identity_image"
    }
}