package com.project.acehotel.features.dashboard.management.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.ui.adapter.inventory.InventoryListAdapter
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentInventoryBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InventoryFragment : Fragment() {
    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchListInventory()

//        inventoryViewModel.getTokens().observe(this) {
//            Timber.tag("TEST").e(it.toString())
//            Timber.tag("TEST").e("TEST")
//        }
    }

    private fun fetchListInventory() {
        inventoryViewModel.fetchListInventory().observe(this) { inventory ->
            when (inventory) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(requireContext())) {
                        activity?.showToast(getString(R.string.check_internet))
                    } else {
                        activity?.showToast(inventory.message.toString())
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("InventoryFragment").d(inventory.message)
                }
                is Resource.Success -> {
                    showLoading(false)

                    initInventoryRecyclerView(inventory.data)
                }
            }
        }
    }

    private fun initInventoryRecyclerView(listInventory: List<Inventory>?) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListInventory.layoutManager = layoutManager

        val adapter = InventoryListAdapter(listInventory)
        binding.rvListInventory.adapter = adapter

        adapter.setOnItemClickCallback(object : InventoryListAdapter.OnItemClickCallback {
            override fun onItemClicked() {

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}