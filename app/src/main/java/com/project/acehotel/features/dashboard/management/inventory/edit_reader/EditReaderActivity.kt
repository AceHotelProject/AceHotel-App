package com.project.acehotel.features.dashboard.management.inventory.edit_reader

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Reader
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.READER_NAME
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityEditReaderBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EditReaderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditReaderBinding

    private val editReaderViewModel: EditReaderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        initReaderData()

        handleButtonBack()

        enableRefresh(false)

        handleEditText()

        handleSaveButton()
    }

    private fun initReaderData() {
        val readerData = Gson().fromJson(intent.getStringExtra(READER_DATA), Reader::class.java)

        binding.apply {
            edEditReaderPower.setText(readerData.powerGain.toString())
            edEditReaderRead.setText(readerData.readInterval.toString())
            tbReader.isChecked = readerData.isActive
            edEditReaderDate.setText(DateUtils.getDateThisDay2())
        }
    }

    private fun handleEditText() {
        binding.apply {
            edEditReaderPower.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutEditReaderPower.error = getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutEditReaderPower.error = null
                    }
                }
            })

            edEditReaderRead.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutEditReaderRead.error = getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutEditReaderRead.error = null
                    }
                }
            })
        }
    }

    private fun handleSaveButton() {
        checkForms()

        binding.apply {
            btnSave.setOnClickListener {
                val powerGain = edEditReaderPower.text.toString()
                val readInterval = edEditReaderRead.text.toString()
                val isReaderActive = tbReader.isChecked

                editReaderViewModel.setQueryTag(READER_NAME, isReaderActive)
                    .observe(this@EditReaderActivity) { reader ->
                        when (reader) {
                            is Resource.Error -> {
                                showLoading(false)
                                isButtonEnabled(true)

                                if (!isInternetAvailable(this@EditReaderActivity)) {
                                    showToast(getString(R.string.check_internet))
                                } else {
                                    showToast(reader.message.toString())
                                }
                            }
                            is Resource.Loading -> {
                                showLoading(true)
                                isButtonEnabled(false)
                            }
                            is Resource.Message -> {
                                showLoading(false)
                                isButtonEnabled(true)

                                Timber.tag("EditReaderActivity").d(reader.message)
                            }
                            is Resource.Success -> {
                                showLoading(false)
                                isButtonEnabled(true)
                            }
                        }
                    }

                editReaderViewModel.updateReader(
                    READER_NAME,
                    powerGain.toInt(),
                    readInterval.toInt()
                ).observe(this@EditReaderActivity) { reader ->
                    when (reader) {
                        is Resource.Error -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            if (!isInternetAvailable(this@EditReaderActivity)) {
                                showToast(getString(R.string.check_internet))
                            } else {
                                showToast(reader.message.toString())
                            }
                        }
                        is Resource.Loading -> {
                            showLoading(true)
                            isButtonEnabled(false)
                        }
                        is Resource.Message -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            Timber.tag("EditReaderActivity").d(reader.message)
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            showToast("Data reader berhasil diubah")
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun checkForms() {
        binding.apply {
            val powerGain = edEditReaderPower.text.toString()
            val readInterval = edEditReaderRead.text.toString()

            isButtonEnabled(powerGain.isNotEmpty() && readInterval.isNotEmpty())
        }
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnSave.isEnabled = isEnabled
    }

    private fun enableRefresh(isDisable: Boolean) {
        binding.refEditReader.isEnabled = isDisable
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refEditReader.isRefreshing = isLoading
    }

    companion object {
        private const val READER_DATA = "reader_data"
    }
}