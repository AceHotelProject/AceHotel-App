package com.project.acehotel.features.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.acehotel.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonLogin()
    }

    private fun handleButtonLogin() {
        binding.btnLogin.setOnClickListener {

        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}