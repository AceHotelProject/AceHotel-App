package com.project.acehotel.features.login

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityLoginBinding
import com.project.acehotel.features.dashboard.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isButtonEnabled(false)

        setupActionBar()

        handleEditText()

        handleButtonLogin()

        handleContactButton()
    }

    private fun handleContactButton() {
        binding.tvContactUs.setOnClickListener {
            // wa link provided by: https://create.wa.link/
            val intentToOpenBrowser = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    CUSTOMER_SERVICE_PHONE
                )
            )
            startActivity(intentToOpenBrowser)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEditText() {
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
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

        binding.edLoginPass.addTextChangedListener(object : TextWatcher {
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

        binding.layoutLoginPass.setEndIconOnClickListener {
            if (binding.edLoginPass.transformationMethod == PasswordTransformationMethod.getInstance()) {
                binding.edLoginPass.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.layoutLoginPass.endIconDrawable = getDrawable(R.drawable.icons_no_see_pass)
            } else {
                binding.edLoginPass.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.layoutLoginPass.endIconDrawable = getDrawable(R.drawable.icons_see_pass)
            }

            binding.edLoginPass.setSelection(binding.edLoginPass.text!!.length)
        }
    }

    private fun handleButtonLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPass.text.toString()

            loginViewModel.loginUser(email, password).observe(this) { result ->
                when (result) {
                    is Resource.Error -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        if (!isInternetAvailable(this)) {
                            showToast(getString(R.string.check_internet))
                        } else {
                            showToast("Pastikan email dan password telah benar")
                        }
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                        isButtonEnabled(false)
                    }
                    is Resource.Message -> {
                        showLoading(false)
                        isButtonEnabled(true)

                        Timber.tag("LoginActivity").d(result.message)
                    }
                    is Resource.Success -> {
                        if (result.data?.tokens != null) {
                            try {
                                loginViewModel.saveAccessToken(result.data.tokens.accessToken.token.toString())
                                loginViewModel.saveRefreshToken(result.data.tokens.refreshToken.token.toString())

                                loginViewModel.insertCacheUser(result.data)
                            } finally {
                                validateToken()
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun validateToken() {
        loginViewModel.getRefreshToken().observe(this) { token ->
            if (token.isNotEmpty() || token != "") {
                showLoading(false)
                isButtonEnabled(true)

                val intentToMainActivity = Intent(this, MainActivity::class.java)
                startActivity(intentToMainActivity)
                finish()
            }
        }
    }

    private fun checkForms() {
        binding.apply {
            val email = edLoginEmail.text.toString()
            val pass = edLoginPass.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.layoutLoginEmail.error = getString(R.string.wrong_email_format)
            } else {
                binding.layoutLoginEmail.error = null
            }

            if (pass.length < 8) {
                binding.layoutLoginPass.error = getString(R.string.wrong_password_format)
            } else {
                binding.layoutLoginPass.error = null
            }

            isButtonEnabled(
                email.isNotEmpty()
                        && pass.isNotEmpty()
                        && pass.length >= 8
                        && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            )
        }
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnLogin.isEnabled = isEnabled
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val CUSTOMER_SERVICE_PHONE = "https://wa.link/anszxf"
    }
}