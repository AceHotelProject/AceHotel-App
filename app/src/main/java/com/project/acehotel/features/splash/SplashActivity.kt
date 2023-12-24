package com.project.acehotel.features.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.BuildConfig
import com.project.acehotel.databinding.ActivitySplashBinding
import com.project.acehotel.features.dashboard.MainActivity
import com.project.acehotel.features.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        initTimber()

        checkUserInfo()
    }

    private fun checkUserInfo() {
        splashViewModel.getUser().observe(this) { result ->
            if (!result.tokens.accessToken.token.isNullOrEmpty()) {
                val intentToHome = Intent(this, MainActivity::class.java)
                startActivity(intentToHome)
                finish()
            } else {
                val intentToLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentToLogin)
                finish()
            }
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}