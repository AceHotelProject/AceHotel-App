package com.project.acehotel.features.dashboard.management.visitor.choose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import com.project.acehotel.features.dashboard.booking.add_booking.AddBookingActivity
import com.project.acehotel.features.dashboard.management.IManagementSearch
import com.project.acehotel.features.dashboard.management.visitor.add.AddVisitorActivity
import com.project.acehotel.features.dashboard.room.checkin.CheckinActivity
import com.project.acehotel.features.dashboard.room.checkout.CheckoutActivity
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChooseVisitorActivity : AppCompatActivity(), IManagementSearch {
    private lateinit var binding: ActivityChooseVisitorBinding
    private val chooseVisitorViewModel: ChooseVisitorViewModel by viewModels()

    private var storedQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        fetchVisitorList("")

        setupSearch()

        handleOnRefresh()

        handleButtonAdd()

        validateToken()
    }

    private fun validateToken() {
        chooseVisitorViewModel.getRefreshToken().observe(this) { token ->
            if (token.isEmpty() || token == "") {
                TokenExpiredDialog().show(supportFragmentManager, "Token Expired Dialog")
            }
        }
    }

    private fun handleButtonAdd() {
        binding.btnAddVisitor.setOnClickListener {
            val intentToAddVisitor = Intent(this, AddVisitorActivity::class.java)
            startActivity(intentToAddVisitor)
        }
    }

    private fun handleOnRefresh() {
        binding.refChooseVisitor.setOnRefreshListener {
            fetchVisitorList("")
        }
    }

    private fun setupSearch() {
        binding.edChooseVisitorSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                fetchVisitorList(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                fetchVisitorList(p0.toString())
            }

        })

        binding.edChooseVisitorSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fetchVisitorList(binding.edChooseVisitorSearch.text.toString())

                binding.edChooseVisitorSearch.clearFocus()
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
                currentFocus?.clearFocus()
                true // consume the action
            } else {
                false // pass on to other listeners.
            }
        }
    }

    private fun fetchVisitorList(
        query: String
    ) {
        var name = ""
        var email = ""
        var identityNum = ""

        if (query.contains("@gmail.com", ignoreCase = true)) {
            email = query
        } else if (query.all { it.isDigit() }) {
            identityNum = query
        } else {
            name = query
        }

        chooseVisitorViewModel.executeGetVisitorList(name, email, identityNum)
            .observe(this) { visitor ->
                when (visitor) {
                    is Resource.Error -> {
                        showLoading(false)

                        if (!isInternetAvailable(this)) {
                            showToast(getString(R.string.check_internet))
                        } else {
                            if (visitor.message?.contains("404", false) == true) {
                                initVisitorRecyclerView(listOf())
                            } else {
                                showToast(visitor.message.toString())
                            }
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

        adapter.setOnItemClickCallback(object : VisitorListAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String, name: String) {
                when (intent.getStringExtra(FLAG_VISITOR)) {
                    MENU_BOOKING -> {
                        val intentToAddBooking =
                            Intent(this@ChooseVisitorActivity, AddBookingActivity::class.java)
                        intentToAddBooking.putExtra(VISITOR_NAME, name)
                        intentToAddBooking.putExtra(VISITOR_ID, id)

                        startActivity(intentToAddBooking)
                    }

                    MENU_CHECKIN -> {
                        val intentToCheckin =
                            Intent(this@ChooseVisitorActivity, CheckinActivity::class.java)
                        startActivity(intentToCheckin)
                    }

                    MENU_CHECKOUT -> {
                        val intentToCheckout =
                            Intent(this@ChooseVisitorActivity, CheckoutActivity::class.java)
                        startActivity(intentToCheckout)
                    }
                }
            }
        })
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

    override fun onSearchQuery(query: String) {
        storedQuery = query

        fetchVisitorList(query)
    }

    companion object {
        private const val FLAG_VISITOR = "flag_visitor"
        private const val VISITOR_NAME = "name_visitor"
        private const val VISITOR_ID = "name_id"

        private const val MENU_BOOKING = "booking"
        private const val MENU_CHECKIN = "checkin"
        private const val MENU_CHECKOUT = "checkout"
    }
}