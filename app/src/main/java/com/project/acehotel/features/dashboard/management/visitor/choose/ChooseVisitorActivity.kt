package com.project.acehotel.features.dashboard.management.visitor.choose

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.visitor.model.Visitor
import com.project.acehotel.core.ui.adapter.visitor.VisitorListAdapter
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityChooseVisitorBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChooseVisitorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseVisitorBinding
    private val chooseVisitorViewModel: ChooseVisitorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        fetchVisitorList()
    }

    private fun fetchVisitorList() {
        chooseVisitorViewModel.getVisitorList().observe(this) { visitor ->
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
                    Timber.tag("VisitorFragment").d(visitor.message)
                }
                is Resource.Success -> {
                    showLoading(false)

                    initVisitorRecyclerView(visitor.data)
                }
            }
        }
    }

    private fun initVisitorRecyclerView(data: List<Visitor>?) {
        val adapter = VisitorListAdapter(data)
        binding.rvChooseVisitor.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvChooseVisitor.layoutManager = layoutManager
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refChooseVisitor.isRefreshing = isLoading
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}