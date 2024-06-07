package com.project.acehotel.features.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivitySplashBinding
import com.project.acehotel.features.dashboard.MainActivity
import com.project.acehotel.features.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

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

        checkUserInfo()
    }

    private fun checkUserInfo() {
        splashViewModel.getAccessToken().observe(this) { token ->
            if (token != "") {
                Handler().postDelayed({
                    val intentToHome = Intent(this, MainActivity::class.java)
                    intentToHome.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentToHome)
                }, DELAY.toLong())
            } else {
                Handler().postDelayed({
                    val intentToLogin = Intent(this, LoginActivity::class.java)
                    intentToLogin.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentToLogin)
                }, DELAY.toLong())
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val DELAY = 4000
    }
}