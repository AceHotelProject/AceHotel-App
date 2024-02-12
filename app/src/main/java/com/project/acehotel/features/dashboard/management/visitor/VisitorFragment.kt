package com.project.acehotel.features.dashboard.management.visitor

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
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class VisitorFragment : Fragment() {
    private var _binding: FragmentVisitorBinding? = null
    private val binding get() = _binding!!

    private val visitorViewModel: VisitorViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchVisitorList()

        handleRefresh()
    }

    private fun handleRefresh() {
        binding.refVisitor.setOnRefreshListener {
            fetchVisitorList()
        }
    }

    private fun fetchVisitorList() {
        visitorViewModel.getVisitorList().observe(this) { visitor ->
            when (visitor) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(requireContext())) {
                        activity?.showToast(getString(R.string.check_internet))
                    } else {
                        activity?.showToast(visitor.message.toString())
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
        binding.rvListVisitor.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListVisitor.layoutManager = layoutManager
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
}