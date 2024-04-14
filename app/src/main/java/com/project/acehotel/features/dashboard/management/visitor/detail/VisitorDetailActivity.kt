package com.project.acehotel.features.dashboard.management.visitor.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.adapter.booking.BookingPagingListAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.DeleteDialogType
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityVisitorDetailBinding
import com.project.acehotel.features.dashboard.booking.detail.BookingDetailActivity
import com.project.acehotel.features.dashboard.management.visitor.add.AddVisitorActivity
import com.project.acehotel.features.popup.delete.DeleteItemDialog
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class VisitorDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVisitorDetailBinding

    private val visitorDetailViewModel: VisitorDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVisitorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        fetchVisitorDetail()

        handleButtonBack()

        setupButtonMore()

        fetchBookingPagingList()

        validateToken()
    }

    private fun validateToken() {
        visitorDetailViewModel.getRefreshToken().observe(this) { token ->
            if (token.isEmpty() || token == "") {
                TokenExpiredDialog().show(supportFragmentManager, "Token Expired Dialog")
            }
        }
    }

    private fun fetchBookingPagingList() {
        val adapter = BookingPagingListAdapter()
        binding.rvVisitorHistory.adapter = adapter

        adapter.addLoadStateListener { loadStates ->
            val isRefreshing =
                loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
            binding.refVisitorDetail.isRefreshing = isRefreshing
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvVisitorHistory.layoutManager = layoutManager

        val filterDate = DateUtils.getDateThisYear()
        visitorDetailViewModel.getPagingListBookingByVisitor(
            intent.getStringExtra(VISITOR_ID) ?: "",
            filterDate,
            true
        )
            .observe(this) { booking ->
                lifecycleScope.launch {
                    adapter.submitData(booking)
                }
            }

        adapter.setOnItemClickCallback(object : BookingPagingListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, booking: Booking) {
                val intentToBookingDetail =
                    Intent(this@VisitorDetailActivity, BookingDetailActivity::class.java)
                val dataToJson = Gson().toJson(booking)

                intentToBookingDetail.putExtra(BOOKING_DATA, dataToJson)
                startActivity(intentToBookingDetail)
            }
        })
    }

    private fun setupButtonMore() {
        binding.btnVisitorDetailMore.setOnClickListener {
            val popUpMenu = PopupMenu(this, binding.btnVisitorDetailMore)
            popUpMenu.menuInflater.inflate(R.menu.menu_detail_item, popUpMenu.menu)

            popUpMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuUpdate -> {
                        val intentToUpdateVisitor = Intent(this, AddVisitorActivity::class.java)
                        intentToUpdateVisitor.putExtra(VISITOR_UPDATE, true)
                        intentToUpdateVisitor.putExtra(
                            VISITOR_ID,
                            intent.getStringExtra(VISITOR_ID) ?: ""
                        )
                        startActivity(intentToUpdateVisitor)

                        true
                    }
                    R.id.menuDelete -> {
                        DeleteItemDialog(
                            DeleteDialogType.VISITOR_DETAIL,
                            intent.getStringExtra(VISITOR_ID) ?: ""
                        ).show(
                            supportFragmentManager,
                            "Delete Dialog"
                        )
                        true
                    }
                    else -> false
                }
            }

            popUpMenu.show()
        }
    }

    private fun fetchVisitorDetail() {
        visitorDetailViewModel.getVisitorDetail(intent.getStringExtra(VISITOR_ID) ?: "")
            .observe(this) { visitor ->
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
                        showLoading(false)

                        binding.apply {
                            if (visitor.data != null) {
                                tvVisitorDetailAddress.text = visitor.data.address
                                tvVisitorDetailEmail.text = visitor.data.email
                                tvVisitorDetailName.text = visitor.data.name
                                tvVisitorDetailNik.text = visitor.data.identity_num
                                tvVisitorDetailPhone.text = visitor.data.phone

                                Glide.with(this@VisitorDetailActivity)
                                    .load(visitor.data.identityImage)
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

    companion object {
        private const val VISITOR_ID = "visitor_id"
        private const val VISITOR_UPDATE = "visitor_update"
        private const val BOOKING_DATA = "booking_data"
    }
}