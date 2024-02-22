package com.project.acehotel.features.dashboard.management.visitor.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityVisitorDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class VisitorDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVisitorDetailBinding
    private val visitorViewModel: VisitorDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVisitorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        fetchVisitorDetail()
    }

    private fun fetchVisitorDetail() {
        visitorViewModel.executeGetVisitorDetail().observe(this) { visitor ->
            when (visitor) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        showToast(visitor.message.toString())
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)

                    Timber.tag("AddFranchiseActivity").d(visitor.message)
                }
                is Resource.Success -> {
                    binding.apply {
                        if (visitor.data != null) {
                            tvVisitorDetailAddress.text = visitor.data.address
                            tvVisitorDetailEmail.text = visitor.data.email
                            tvVisitorDetailName.text = visitor.data.name
                            tvVisitorDetailNik.text = visitor.data.identity_num
                            tvVisitorDetailPhone.text = visitor.data.phone
                            
                            Glide.with(this@VisitorDetailActivity).load(visitor.data.identityImage)
                                .into(ivVisitorDetail)
                        }
                    }
                }
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refVisitorDetail.isRefreshing = isLoading
    }

    private fun handleButtonBack() {
        binding.btnVisitorDetailBack.setOnClickListener {
            finish()
        }
    }
}