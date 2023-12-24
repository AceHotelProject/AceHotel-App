package com.project.acehotel.features.login

import android.annotation.SuppressLint
import android.content.Intent
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
import com.project.acehotel.core.utils.showLongToast
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

        setupActionBar()

        handleEditText()

        handleButtonLogin()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEditText() {
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!Patterns.EMAIL_ADDRESS.matcher(p0!!).matches()) {
                    isButtonEnabled(false)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!Patterns.EMAIL_ADDRESS.matcher(p0!!).matches()) {
                    isButtonEnabled(false)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (Patterns.EMAIL_ADDRESS.matcher(p0!!).matches()) {
                    isButtonEnabled(true)
                    binding.layoutLoginEmail.error = null
                } else {
                    isButtonEnabled(false)
                    binding.layoutLoginEmail.error = getString(R.string.wrong_email_format)
                }
            }
        })

        binding.edLoginPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isEmpty()) {
                    isButtonEnabled(false)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length < 8) {
                    isButtonEnabled(false)
                    binding.layoutLoginPass.error = getString(R.string.wrong_password_format)
                } else {
                    isButtonEnabled(true)
                    binding.layoutLoginPass.error = null
                }
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
                        showLongToast(result.message.toString())
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Message -> {
                        showLoading(false)
                        Timber.tag("LoginActivity").d(result.message)
                    }
                    is Resource.Success -> {
                        showLoading(false)

                        if (result.data?.tokens != null) {
                            loginViewModel.insertCacheUser(result.data)

                            val intentToMain = Intent(this, MainActivity::class.java)
                            startActivity(intentToMain)
                            finish()
                        }
                    }
                    else -> {}
                }
            }
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
}