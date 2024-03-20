package com.project.acehotel.features.dashboard.profile.stats

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.constants.FilterDate
import com.project.acehotel.core.utils.constants.filterDateList
import com.project.acehotel.core.utils.constants.mapToFilterDateValue
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityOverallStatsBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OverallStatsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverallStatsBinding

    private val overallStatsViewModel: OverallStatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOverallStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleBackButton()

        setupFilter()

        fetchRecap(FilterDate.NOW.value)
    }

    private fun fetchRecap(filterDate: String) {
        overallStatsViewModel.getHotelRecap(filterDate).observe(this) { recap ->
            when (recap) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this@OverallStatsActivity)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        showToast(recap.message.toString())
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("OverallStatsActivity").d(recap.message)
                }
                is Resource.Success -> {
                    showLoading(false)

                    binding.apply {
                        tvRecapTotalProfit.text = recap.data?.revenue.toString()
                        tvRecapHotelFranchise.text = recap.data?.branchCount.toString()
                        tvRecapTotalCheckin.text = recap.data?.checkinCount.toString()
                        tvRecapTotalBooking.text = recap.data?.totalBooking.toString()
                    }
                }
            }
        }
    }

    private fun setupFilter() {
        val adapter = ArrayAdapter(this, R.layout.drop_inventory_item, filterDateList)

        binding.edAddItemType.apply {
            setAdapter(adapter)

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    binding.layoutAddItemType.isHintEnabled = p0.isNullOrEmpty()

                    fetchRecap(mapToFilterDateValue(p0.toString()))
                }
            })
        }
    }

    private fun handleBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refRecap.isRefreshing = isLoading
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}