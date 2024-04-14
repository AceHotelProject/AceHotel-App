package com.project.acehotel.features.dashboard.management.visitor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.visitor.model.Visitor
import com.project.acehotel.core.ui.adapter.visitor.VisitorListAdapter
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentVisitorBinding
import com.project.acehotel.features.dashboard.management.IManagementSearch
import com.project.acehotel.features.dashboard.management.visitor.detail.VisitorDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class VisitorFragment : Fragment(), IManagementSearch {
    private var _binding: FragmentVisitorBinding? = null
    private val binding get() = _binding!!

    private val visitorViewModel: VisitorViewModel by activityViewModels()
    private var storedQuery = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchVisitorList("")

        handleRefresh()

        binding.tvEmptyBookingNext.visibility = View.VISIBLE
    }

    private fun handleRefresh() {
        binding.refVisitor.setOnRefreshListener {
            fetchVisitorList(storedQuery)
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

        visitorViewModel.executeGetVisitorList(name, email, identityNum)
            .observe(this) { visitor ->
                when (visitor) {
                    is Resource.Error -> {
                        showLoading(false)

                        if (!isInternetAvailable(requireContext())) {
                            activity?.showToast(getString(R.string.check_internet))
                        } else {
                            if (visitor.message?.contains("404", false) == true) {
                                initVisitorRecyclerView(listOf())
                            } else {
                                activity?.showToast(visitor.message.toString())
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

                        handleEmptyStates(visitor.data)
                    }
                }
            }
    }

    private fun handleEmptyStates(data: List<Visitor>?) {
        binding.tvEmptyBookingNext.visibility =
            if (data?.isEmpty()!!) View.VISIBLE else View.INVISIBLE
    }

    private fun initVisitorRecyclerView(data: List<Visitor>?) {
        val adapter = VisitorListAdapter(data)
        binding.rvListVisitor.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListVisitor.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : VisitorListAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String, name: String) {
                val intentToVisitorDetail =
                    Intent(requireContext(), VisitorDetailActivity::class.java)
                intentToVisitorDetail.putExtra(VISITOR_ID, id)
                startActivity(intentToVisitorDetail)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitorBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refVisitor.isRefreshing = isLoading
    }

    override fun onSearchQuery(query: String) {
        storedQuery = query

        fetchVisitorList(storedQuery)
    }

    companion object {
        private const val VISITOR_ID = "visitor_id"
    }
}