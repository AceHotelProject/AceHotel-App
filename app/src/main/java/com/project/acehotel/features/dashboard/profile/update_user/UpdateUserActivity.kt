package com.project.acehotel.features.dashboard.profile.update_user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.utils.constants.mapToUserDisplay
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityUpdateUserBinding
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UpdateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserBinding

    private val updateUserViewModel: UpdateUserViewModel by viewModels()

    private lateinit var userData: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        initUserData()

        handleButtonBack()

        enableRefresh(false)
        isButtonEnabled(false)

        validateToken()

        handleEditText()

        handleSaveButton()
    }

    private fun handleEditText() {
        binding.apply {
            edUpdateUserName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()

                    if (p0.toString() != userData.username) {
                        isButtonEnabled(true)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutUpdateUserName.error = getString(R.string.field_cant_empty)
                    } else {
                        if (p0.toString() != userData.username) {
                            isButtonEnabled(true)
                        }
                        binding.layoutUpdateUserName.error = null
                    }
                }
            })

            edUpdateUserEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()

                    if (p0.toString() != userData.email) {
                        isButtonEnabled(true)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutUpdateUserEmail.error = getString(R.string.field_cant_empty)
                    } else {
                        if (p0.toString() != userData.email) {
                            isButtonEnabled(true)
                        }
                        binding.layoutUpdateUserEmail.error = null
                    }
                }
            })
        }
    }

    private fun handleSaveButton() {
        checkForms()

        binding.btnSave.setOnClickListener {
            val role = userData.role?.role
            val name = binding.edUpdateUserName.text.toString()
            val email = binding.edUpdateUserEmail.text.toString()

            updateUserViewModel.executeUpdateUser(userData.id ?: "", email, name, role ?: "")
                .observe(this) { user ->
                    when (user) {
                        is Resource.Error -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            if (!isInternetAvailable(this@UpdateUserActivity)) {
                                showToast(getString(R.string.check_internet))
                            } else {
                                showToast(user.message.toString())
                            }
                        }
                        is Resource.Loading -> {
                            showLoading(true)
                            isButtonEnabled(false)
                        }
                        is Resource.Message -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            Timber.tag("UpdateUserActivity").d(user.message)
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            isButtonEnabled(true)

                            showToast("Data user berhasil dirubah")
                            finish()
                        }
                    }
                }
        }
    }

    private fun checkForms() {
        binding.apply {
            val email = binding.edUpdateUserEmail.text.toString()
            val name = binding.edUpdateUserName.text.toString()

            isButtonEnabled(email.isNotEmpty() && name.isNotEmpty() && email != userData.email && name != userData.username)
        }
    }

    private fun initUserData() {
        val data = intent.getStringExtra(USER_DATA)
        userData = Gson().fromJson(data, User::class.java)

        binding.apply {
            edUpdateUserRole.setText(mapToUserDisplay(userData.role?.role ?: "role"))
            edUpdateUserEmail.setText(userData.email)
            edUpdateUserName.setText(userData.username)
        }

        isEditTextEditable(binding.edUpdateUserRole, false)
    }

    private fun validateToken() {
        updateUserViewModel.getRefreshToken().observe(this) { token ->
            if (token.isEmpty() || token == "") {
                TokenExpiredDialog().show(supportFragmentManager, "Token Expired Dialog")
            }
        }
    }

    private fun isEditTextEditable(editText: TextInputEditText, isEditable: Boolean) {
        editText.isFocusable = isEditable
        editText.isClickable = isEditable
        editText.isFocusableInTouchMode = isEditable
        editText.isCursorVisible = isEditable
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
        binding.refUpdateInventory.isRefreshing = isLoading
    }

    private fun enableRefresh(isEnable: Boolean) {
        binding.refUpdateInventory.isEnabled = isEnable
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val USER_DATA = "user_data"
    }
}