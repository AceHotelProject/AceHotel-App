package com.project.acehotel.features.dashboard.profile.manage_user

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.ui.adapter.user.ManageUserAdapter
import com.project.acehotel.core.utils.constants.UserRole
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityManageUserBinding
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ManageUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageUserBinding

    private val manageUserViewModel: ManageUserViewModel by viewModels()

    private var currentUserRole: UserRole? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManageUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        fetchListUser()

        handleEmptyStates(listOf())

        validateToken()

        handleRefresh()

        checkUserRole()
    }

    private fun checkUserRole() {
        manageUserViewModel.getUser().observe(this) { user ->
            if (user != null) {
                currentUserRole = user.user?.role
            }
        }
    }

    private fun handleRefresh() {
        binding.refFranchiseUser.setOnRefreshListener {
            fetchListUser()
        }
    }

    private fun fetchListUser() {
        manageUserViewModel.executeGetUserByHotel().observe(this) { user ->
            when (user) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        showToast(user.message.toString())
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("ManageFranchiseActivity").d(user.message)
                }
                is Resource.Success -> {
                    showLoading(false)

                    if (currentUserRole != null) {
                        initUserRecyclerView(user.data, currentUserRole!!)
                    }

                    handleEmptyStates(user.data)
                }
            }
        }
    }

    private fun initUserRecyclerView(data: List<User>?, userRole: UserRole) {
        val filterData = data?.filter { user ->
            if (userRole == UserRole.FRANCHISE) {
                user.role != UserRole.MASTER
            } else {
                true
            }
        }

        val adapter = ManageUserAdapter(filterData, this)
        binding.rvListUser.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvListUser.layoutManager = layoutManager
    }

    private fun handleEmptyStates(data: List<User>?) {
        binding.tvEmptyUser.visibility = if (data?.isEmpty()!!) View.VISIBLE else View.GONE
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun validateToken() {
        manageUserViewModel.getRefreshToken().observe(this) { token ->
            if (token.isEmpty() || token == "") {
                TokenExpiredDialog().show(supportFragmentManager, "Token Expired Dialog")
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refFranchiseUser.isRefreshing = isLoading
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}