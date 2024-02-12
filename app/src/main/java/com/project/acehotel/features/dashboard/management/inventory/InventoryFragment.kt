package com.project.acehotel.features.dashboard.management.inventory

import android.content.Context
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
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.ui.adapter.inventory.InventoryListAdapter
import com.project.acehotel.core.utils.constants.InventoryType
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentInventoryBinding
import com.project.acehotel.features.dashboard.management.inventory.add_item.AddItemInventoryActivity
import com.project.acehotel.features.dashboard.management.inventory.detail.InventoryDetailActivity
import com.project.acehotel.features.popup.token.TokenExpiredDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InventoryFragment : Fragment() {
    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private val inventoryViewModel: InventoryViewModel by activityViewModels()
    private var hotelId: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchListInventory()

        handleRefreshLayout()

        validateToken()

        handleButtonAddInventory()
    }

    private fun handleButtonAddInventory() {
        binding.btnAddInventory.setOnClickListener {
            val intentToAddInventory =
                Intent(requireContext(), AddItemInventoryActivity::class.java)
            startActivity(intentToAddInventory)
        }
    }

    private fun validateToken() {
        inventoryViewModel.getRefreshToken().observe(this) { token ->
            if (token.isNullOrEmpty()) {
                TokenExpiredDialog().show(parentFragmentManager, "Token Expired Dialog")
            }
        }
    }

    private fun handleRefreshLayout() {
        binding.apply {
            svInventory.viewTreeObserver.addOnScrollChangedListener {
                refInventory.isEnabled = svInventory.scrollY == 0
            }

            refInventory.setOnRefreshListener {
                fetchListInventory()
            }
        }
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

                    initQuickInfo(inventory.data)
                }
            }
        }
    }

    private fun initQuickInfo(data: List<Inventory>?) {
        var linenTotal = 0
        var bedTotal = 0

        if (data != null) {
            for (i in data.indices) {
                if (data[i].type == InventoryType.BED.type) {
                    bedTotal += 1
                } else {
                    linenTotal += 1
                }
            }
        }

        binding.apply {
            tvTotalLinen.text = linenTotal.toString()
            tvTotalBed.text = bedTotal.toString()
        }
    }

    private fun initInventoryRecyclerView(listInventory: List<Inventory>?) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListInventory.layoutManager = layoutManager

        val adapter = InventoryListAdapter(listInventory)
        binding.rvListInventory.adapter = adapter

        adapter.setOnItemClickCallback(object : InventoryListAdapter.OnItemClickCallback {
            override fun onItemClicked(
                context: Context,
                id: String,
                name: String,
                type: String,
                stock: Int
            ) {
                val intentToInventoryDetail =
                    Intent(requireContext(), InventoryDetailActivity::class.java)

                intentToInventoryDetail.putExtra(INVENTORY_ITEM_ID, id)
                intentToInventoryDetail.putExtra(INVENTORY_ITEM_NAME, name)
                intentToInventoryDetail.putExtra(INVENTORY_ITEM_TYPE, type)

                startActivity(intentToInventoryDetail)
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
        binding.refInventory.isRefreshing = isLoading
    }

    companion object {
        private const val INVENTORY_ITEM_ID = "inventory_item_id"
        private const val INVENTORY_ITEM_NAME = "inventory_item_name"
        private const val INVENTORY_ITEM_TYPE = "inventory_item_type"
    }
}